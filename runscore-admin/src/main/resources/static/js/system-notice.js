var systemNoticeVM = new Vue({
	el : '#system-notice',
	data : {
		title : '',

		systemNoticeActionTitle : '',
		contentEditor : null,
		addOrUpdateSystemNoticeFlag : false,
		editSystemNotice : {},

		showViewNoticeFlag : false,
		viewNotice : {},
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initTable();
		this.contentEditor = new window.wangEditor('#contentEditor');
		this.contentEditor.customConfig.menus = [ 'head', // 标题
		'bold', // 粗体
		'fontSize', // 字号
		'fontName', // 字体
		'italic', // 斜体
		'underline', // 下划线
		'strikeThrough', // 删除线
		'foreColor', // 文字颜色
		'backColor', // 背景颜色
		'link', // 插入链接
		'list', // 列表
		'justify', // 对齐方式
		'quote', // 引用
		'emoticon', // 表情
		'table', // 表格
		'code', // 插入代码
		'undo', // 撤销
		'redo' // 重复
		];
		this.contentEditor.create();
	},
	methods : {

		showAddSystemNoticeModal : function() {
			this.addOrUpdateSystemNoticeFlag = true;
			this.systemNoticeActionTitle = '新增公告';
			this.editSystemNotice = {
				title : '',
				content : '',
				publishTime : dayjs().format('YYYY-MM-DDTHH:mm')
			};
			this.contentEditor.txt.html('');
		},

		showEditSystemNoticeModal : function(id) {
			var that = this;
			that.$http.get('/systemNotice/findSystemNoticeById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editSystemNotice = res.body.data;
				that.editSystemNotice.publishTime = dayjs(that.editSystemNotice.publishTime).format('YYYY-MM-DDTHH:mm');
				that.addOrUpdateSystemNoticeFlag = true;
				that.systemNoticeActionTitle = '编辑公告';
				this.contentEditor.txt.html(that.editSystemNotice.content);
			});
		},

		addOrUpdateSystemNotice : function() {
			var that = this;
			var editSystemNotice = that.editSystemNotice;
			if (editSystemNotice.title == null || editSystemNotice.title == '') {
				layer.alert('请输入标题', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editSystemNotice.publishTime == null || editSystemNotice.publishTime == '') {
				layer.alert('请输入发布时间', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var content = this.contentEditor.txt.html();
			if (content == null || content == '') {
				layer.alert('请输入内容', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			editSystemNotice.publishTime = dayjs(editSystemNotice.publishTime).format('YYYY-MM-DD HH:mm:ss');
			editSystemNotice.content = content;
			that.$http.post('/systemNotice/addOrUpdateSystemNotice', editSystemNotice, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateSystemNoticeFlag = false;
				that.refreshTable();
			});
		},

		delSystemNotice : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/systemNotice/delSystemNoticeById', {
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
		},

		showViewNoticeModal : function(notice) {
			this.showViewNoticeFlag = true;
			this.viewNotice = notice;
		},

		initTable : function() {
			var that = this;
			$('.system-notice-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/systemNotice/findSystemNoticeByPage',
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
						title : that.title
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
					field : 'title',
					title : '标题'
				}, {
					field : 'createTime',
					title : '创建时间'
				}, {
					field : 'publishTime',
					title : '发布时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="view-notice-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">查看内容</button>', '<button type="button" class="edit-notice-btn btn btn-outline-success btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-notice-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .view-notice-btn' : function(event, value, row, index) {
							that.showViewNoticeModal(row);
						},
						'click .edit-notice-btn' : function(event, value, row, index) {
							that.showEditSystemNoticeModal(row.id);
						},
						'click .del-notice-btn' : function(event, value, row, index) {
							that.delSystemNotice(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.system-notice-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},
	}
});