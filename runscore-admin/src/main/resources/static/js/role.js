var configManageVM = new Vue({
	el : '#role',
	data : {
		name : '',
		description : '',

		addOrUpdateConfigFlag : false,
		fenRoleFlag : false,
		configActionTitle : '',
		editConfig : {},

	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initTable();
	},
	methods : {

		initTable : function() {
			var that = this;
			$('.config-manage-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/role/findRoleList',
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100 ],
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.total,
						rows : res.data
					};
				},
				columns : [{
					field : 'name',
					title : '角色名称'
				}, {
					field : 'description',
					title : '备注'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="view-notice-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">分配权限</button>', '<button type="button" class="edit-notice-btn btn btn-outline-success btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-notice-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .view-notice-btn' : function(event, value, row, index) {
							that.assigningRole(row.id);
						},
						'click .edit-notice-btn' : function(event, value, row, index) {
							that.openEditConfigModal(row.id);
						},
						'click .del-notice-btn' : function(event, value, row, index) {
							that.delConfig(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.config-manage-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		openAddConfigModal : function() {
			this.addOrUpdateConfigFlag = true;
			this.configActionTitle = '新增角色';
			this.editConfig = {
				name : '',
				description : ''
			};
		},

		openEditConfigModal : function(id) {
			var that = this;
			that.$http.get('/role/findJbRoleById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editConfig = res.body.data;
				that.addOrUpdateConfigFlag = true;
				that.configActionTitle = '编辑角色';
			});
		},

		 assigningRole : function(id) {
		   	var title = "分配权限";
		   	var url = "/role-assigning?id=" + id;
		    layer.open({
		        type: 2,
		        title: title,
		        shadeClose: true,
		        shade: 0.8,
		        area: ['250px', '500px'],
		        fix: false,
		        maxmin: true,
		        content: url
		    });
		},

		addOrUpdateConfig : function() {
			var that = this;
			var editConfig = that.editConfig;
			if (editConfig.name == null || editConfig.name == '') {
				layer.alert('请输入角色名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editConfig.description == null || editConfig.description == '') {
				layer.alert('请输入备注', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/role/addOrUpdateRole', editConfig).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateConfigFlag = false;
				that.refreshTable();
			});
		},

		delConfig : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/role/delRoleById', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		}
	}
});