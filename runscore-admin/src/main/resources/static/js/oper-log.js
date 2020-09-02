var operLogVM = new Vue({
	el : '#oper-log',
	data : {
		userName : '',
		ipAddr : '',
		startTime : dayjs().subtract(7, 'day').format('YYYY-MM-DD'),
		endTime : dayjs().format('YYYY-MM-DD'),
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
			$('.oper-log-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/operLog/findOperLogByPage',
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
					field : 'operName',
					title : '操作用户'
				}, {
					field : 'ipAddr',
					title : 'ip地址'
				}, {
					field : 'system',
					title : '系统'
				}, {
					field : 'module',
					title : '模块',
				}, {
					field : 'operate',
					title : '操作'
				}, {
					field : 'requestMethod',
					title : '请求方式'
				}, {
					field : 'requestUrl',
					title : 'url'
				}, {
					field : 'operTime',
					title : '操作时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="request-param-btn btn btn-outline-info btn-sm" style="margin-right: 4px;">查看请求参数</button>' ]
					},
					events : {
						'click .request-param-btn' : function(event, value, row, index) {
							layer.open({
								type : 1,
								anim : 2,
								shadeClose : true,
								area : [ '420px', '240px' ],
								content : '<div style="padding: 10px;">' + '<p>请求参数</p>' + '<div style="color: red; word-break: break-all; word-wrap: break-word;">' + row.requestParam + '</div>' + '</div>'
							});
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.oper-log-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		}
	}
});