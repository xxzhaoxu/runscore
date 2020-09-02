var gatheringChannelVM = new Vue({
	el : '#gathering-channel',
	data : {
		channelCode : '',
		channelName : '',

		addOrUpdateChannelFlag : false,
		channelActionTitle : '',
		editChannel : {},

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
			$('.gathering-channel-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/gatheringChannel/findGatheringChannelByPage',
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100 ],
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber,
						channelCode : that.channelCode,
						channelName : that.channelName
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				columns : [ {
					field : 'channelCode',
					title : '通道code'
				}, {
					field : 'channelName',
					title : '通道名称'
				}, {
					field : 'defaultReceiveOrderRabate',
					title : '默认接单返点'
				}, {
					title : '是否启用',
					formatter : function(value, row, index) {
						return row.enabled ? '启用' : '禁用';
					}
				}, {
					field : 'createTime',
					title : '创建时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="edit-channel-btn btn btn-outline-success btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-channel-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .edit-channel-btn' : function(event, value, row, index) {
							that.showEditChannelModal(row.id);
						},
						'click .del-channel-btn' : function(event, value, row, index) {
							that.delChannel(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.gathering-channel-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		showAddChannelModal : function() {
			this.addOrUpdateChannelFlag = true;
			this.channelActionTitle = '新增通道';
			this.editChannel = {
				channelCode : '',
				channelName : '',
				enabled : true
			};
		},

		showEditChannelModal : function(id) {
			var that = this;
			that.$http.get('/gatheringChannel/findGatheringChannelById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editChannel = res.body.data;
				that.addOrUpdateChannelFlag = true;
				that.channelActionTitle = '编辑通道';
			});
		},

		addOrUpdateChannel : function() {
			var that = this;
			var editChannel = that.editChannel;
			if (editChannel.channelCode === null || editChannel.channelCode === '') {
				layer.alert('请输入通道code', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editChannel.channelName === null || editChannel.channelName === '') {
				layer.alert('请输入通道名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editChannel.defaultReceiveOrderRabate === null || editChannel.defaultReceiveOrderRabate === '') {
				layer.alert('请输入默认接单返点', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/gatheringChannel/addOrUpdateGatheringChannel', editChannel, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateChannelFlag = false;
				that.refreshTable();
			});
		},

		delChannel : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/gatheringChannel/delGatheringChannelById', {
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