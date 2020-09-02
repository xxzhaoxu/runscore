var platformOrderVM = new Vue({
	el : '#platform-order',
	data : {
		global : GLOBAL,
		receiverUserName : '',
	},
	computed : {

	},
	created : function() {
	},
	mounted : function() {

		this.initTable();
	},
	methods : {

		initTable : function() {
			var that = this;
			$('.platform-order-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/statisticalAnalysis/findMerchantTradeResultsSituation',
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
						receiverUserName : that.receiverUserName,
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
					title : '总代理名称',
					formatter : function(value, row, index, field) {
						var text = row.userName;
						return text;
					}
				}, {
					field : 'todayTradeAmount',
					title : '今天收款'
				}, {
					field : 'todayOrderNum',
					title : '今天订单'
				}, {
					field : 'todayPaidOrderNum',
					title : '今天成交'
				}, {
					field : 'yesterdayTradeAmount',
					title : '昨天收款'
				}, {
					field : 'yesterdayOrderNum',
					title : '昨天订单'
				}, {
					field : 'yesterdayPaidOrderNum',
					title : '昨天成交'
				}, {
					field : 'monthTradeAmount',
					title : '本月收款'
				}, {
					field : 'monthOrderNum',
					title : '本月订单'
				}, {
					field : 'monthPaidOrderNum',
					title : '本月成交'
				}, {
					field : 'totalTradeAmount',
					title : '累计收款'
				}, {
					field : 'totalOrderNum',
					title : '累计订单'
				}, {
					field : 'totalPaidOrderNum',
					title : '累计成交'
				}]
			});
		},

		refreshTable : function() {
			$('.platform-order-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		}
	}
});