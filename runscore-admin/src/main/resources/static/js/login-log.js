var loginLogVM = new Vue({
	el : '#login-log',
	data : {
		userName : '',
		ipAddr : '',
		state : '',
		loginStateDictItems : [],
		startTime : dayjs().subtract(7, 'day').format('YYYY-MM-DD'),
		endTime : dayjs().format('YYYY-MM-DD'),
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
				url : '/loginLog/findLoginLogByPage',
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
						ipAddr : that.ipAddr,
						state : that.state,
						startTime : that.startTime,
						endTime : that.endTime
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
					field : 'stateName',
					title : '登录状态'
				}, {
					field : 'msg',
					title : '登录信息'
				}, {
					field : 'loginTime',
					title : '登录时间'
				}, {
					field : 'lastAccessTime',
					title : '最后访问时间'
				} ]
			});
		},
		refreshTable : function() {
			$('.login-log-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		onlineAccount : function() {
			var that = this;
			that.$http.get('/loginLog/findOnlineAccountByPage', {
				params : {
					pageSize : 10,
					pageNum : 1
				}
			}).then(function(res) {
				if (res.body.data != null) {
				}
			});
		}
	}
});