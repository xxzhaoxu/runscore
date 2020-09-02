var withdrawRecordVM = new Vue({
	el : '#withdraw-record',
	data : {
		orderNo : '',
		merchantId : '',
		merchants : [],
		state : '',
		merchantSettlementStateDictItems : [],
		applyStartTime : dayjs().format('YYYY-MM-DD'),
		applyEndTime : dayjs().format('YYYY-MM-DD'),

		approvalFlag : false,
		onlyShowNotApprovedBtnFlag : false,
		selectedRecord : {},
		note : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var state = getQueryString('state');
		if (state == '1') {
			this.state = state;
			this.applyStartTime = '';
			this.applyEndTime = '';
		}
		var todoId = getQueryString('todoId');
		if (todoId != null) {
			this.showApprovalModal(todoId);
		}

		this.loadAllMerchant();
		this.loadMerchantSettlementStateDictItem();
		this.initTable();
	},
	methods : {

		loadAllMerchant : function() {
			var that = this;
			that.$http.get('/merchant/findAllMerchant').then(function(res) {
				this.merchants = res.body.data;
			});
		},

		loadMerchantSettlementStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'merchantSettlementState'
				}
			}).then(function(res) {
				this.merchantSettlementStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.merchant-settlement-record-table').bootstrapTable({
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
						orderNo : that.orderNo,
						merchantId : that.merchantId,
						state : that.state,
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
				detailView : true,
				detailFormatter : function(index, row, element) {
					var html = template('record-detail', {
						record : row
					});
					return html;
				},
				columns : [ {
					field : 'orderNo',
					title : '订单号'
				}, {
					field : 'merchantName',
					title : '商户'
				}, {
					field : 'withdrawAmount',
					title : '提现金额'
				}, {
					title : '结算银行卡',
					formatter : function(value, row, index) {
						return row.openAccountBank + '/' + row.accountHolder + '/' + row.bankCardAccount;
					}
				}, {
					field : 'stateName',
					title : '状态'
				}, {
					field : 'applyTime',
					title : '申请时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						if (row.state == '1') {
							return [ '<button type="button" class="withdraw-approval-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">进行审核</button>' ].join('');
						} else if (row.state == '2') {
							return [ '<button type="button" class="confirm-credited-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">确认到帐</button>', '<button type="button" class="not-approved-btn btn btn-outline-secondary btn-sm">审核不通过</button>' ].join('');
						}
					},
					events : {
						'click .withdraw-approval-btn' : function(event, value, row, index) {
							that.onlyShowNotApprovedBtnFlag = false;
							that.showApprovalModal(row.id);
						},
						'click .confirm-credited-btn' : function(event, value, row, index) {
							that.confirmCredited(row.id);
						},
						'click .not-approved-btn' : function(event, value, row, index) {
							that.onlyShowNotApprovedBtnFlag = true;
							that.showApprovalModal(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.merchant-settlement-record-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		showApprovalModal : function(id) {
			var that = this;
			that.$http.get('/merchant/findByMerchantSettlementRecordId', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedRecord = res.body.data;
				that.note = null;
				that.approvalFlag = true;
			});

		},

		approved : function() {
			var that = this;
			layer.confirm('确定审核通过吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchant/settlementApproved', {
					params : {
						id : that.selectedRecord.id,
						note : that.note
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.approvalFlag = false;
					that.refreshTable();
				});
			});
		},

		notApproved : function() {
			var that = this;
			layer.confirm('确定审核不通过吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchant/settlementNotApproved', {
					params : {
						id : that.selectedRecord.id,
						note : that.note
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.approvalFlag = false;
					that.refreshTable();
				});
			});
		},

		confirmCredited : function(id) {
			var that = this;
			layer.confirm('确认是已到帐了吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchant/settlementConfirmCredited', {
					params : {
						id : id,
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