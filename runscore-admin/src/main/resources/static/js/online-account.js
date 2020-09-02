var loginLogVM = new Vue({
	el : '#login-log',
	data : {
		userName : '',
		ipAddr : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadLoginStateDictItem();
		this.initTable();
	},
	methods : {
		loadLoginStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'loginState'
				}
			}).then(function(res) {
				this.loginStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.login-log-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/loginLog/findOnlineAccountByPage',
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
						userName : that.userName,
						ipAddr : that.ipAddr
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
					field : 'sessionId',
					title : '会话id'
				}, {
					field : 'userName',
					title : '登录用户'
				}, {
					field : 'systemName',
					title : '登录系统'
				}, {
					field : 'ipAddr',
					title : 'ip地址',
				}, {
					field : 'loginLocation',
					title : '登录地点'
				}, {
					field : 'browser',
					title : '浏览器'
				}, {
					field : 'os',
					title : '操作系统'
				}, {
					field : 'loginTime',
					title : '登录时间'
				}, {
					field : 'lastAccessTime',
					title : '最后访问时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="session-logout-btn btn btn-outline-danger btn-sm">退出登录</button>' ].join('');
					},
					events : {
						'click .session-logout-btn' : function(event, value, row, index) {
							that.logout(row.sessionId);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.login-log-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		logout : function(sessionId) {
			var that = this;
			that.$http.get('/loginLog/logout', {
				params : {
					sessionId : sessionId
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.refreshTable();
			});
		}
	}
});