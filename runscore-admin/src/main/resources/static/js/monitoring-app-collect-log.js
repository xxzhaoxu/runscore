var operLogVM = new Vue({
	el : '#monitoring-app-collect-log',
	data : {
		userName : '',
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
			$('.monitoring-app-collect-log-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/monitoringAppCollectLog/findMonitoringAppCollectLogByPage',
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
					title : '账号名'
				}, {
					field : 'secretKey',
					title : '账号密钥',
				}, {
					field : 'alipayPayee',
					title : '收款人',
					formatter : function(value, row, index, field) {
						var text = "";
						if (row.alipayPayee != null && row.alipayPayee != "") {
							text += '支付宝：' + row.alipayPayee;
						}else if (row.wechatPayee != null && row.wechatPayee != ""){
							text += '微信：' + row.wechatPayee;
						}else{
							text += '银行卡：' + row.bankPayee;
						}
						return text;
					}
				}, {
					field : 'amount',
					title : '收款金额',
					formatter : function(value, row, index, field) {
						return row.amount+"元";
					}
				}, {
					field : 'createTime',
					title : '创建时间'
				} ]
			});
		},

		refreshTable : function() {
			$('.monitoring-app-collect-log-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		}
	}
});