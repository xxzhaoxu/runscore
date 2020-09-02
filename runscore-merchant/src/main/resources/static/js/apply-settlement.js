var applySettlementVM = new Vue({
	el : '#apply-settlement',
	data : {
		global : GLOBAL,
		selectedTab : 'applySettlement',
		withdrawableAmount : '',
		merchantBankCards : [],
		withdrawAmount : '',
		merchantBankCardId : '',
		moneyPwd : '',

		applyStartTime : '',
		applyEndTime : '',

	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadWithdrawableAmount();
		this.loadMerchantBankCard();
		this.initTable();
	},
	methods : {

		loadWithdrawableAmount : function() {
			var that = this;
			that.$http.get('/merchant/getMerchantInfo').then(function(res) {
				that.withdrawableAmount = res.body.data.withdrawableAmount;
			});
		},

		loadMerchantBankCard : function() {
			var that = this;
			that.$http.get('/merchant/findMerchantBankCardByMerchantId').then(function(res) {
				that.merchantBankCards = res.body.data;
			});
		},

		applySettlement : function() {
			var that = this;
			if (that.withdrawAmount === null || that.withdrawAmount === '') {
				layer.alert('请输入提现金额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.merchantBankCardId === null || that.merchantBankCardId === '') {
				layer.alert('请选择结算银行卡', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.moneyPwd === null || that.moneyPwd === '') {
				layer.alert('请输入资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/merchant/applySettlement', {
				withdrawAmount : that.withdrawAmount,
				merchantBankCardId : that.merchantBankCardId,
				moneyPwd : that.moneyPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.withdrawAmount = '';
				that.merchantBankCardId = '';
				that.moneyPwd = '';
				that.loadWithdrawableAmount();
			});
		},

		initTable : function() {
			var that = this;
			$('.settlement-record-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/merchant/findMerchantSettlementRecordByPage',
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
						applyStartTime : that.applyStartTime,
						applyEndTime : that.applyEndTime
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
					field : 'orderNo',
					title : '订单号'
				}, {
					field : 'withdrawAmount',
					title : '提现金额'
				}, {
					field : 'applyTime',
					title : '申请时间'
				}, {
					field : 'openAccountBank',
					title : '开户银行'
				}, {
					field : 'accountHolder',
					title : '开户姓名'
				}, {
					field : 'bankCardAccount',
					title : '银行卡号'
				}, {
					field : 'stateName',
					title : '状态'
				} ]
			});
		},

		refreshTable : function() {
			$('.settlement-record-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

	}
});