var gatheringCodeVM = new Vue({
	el : '#gathering-code',
	data : {
		global : GLOBAL,
		state : '',
		gatheringCodeStateDictItems : [],
		gatheringChannelId : '',
		gatheringChannelDictItems : [],
		payee : '',
		userName : '',
		inviterUserName : '',

		addOrUpdateGatheringCodeFlag : false,
		gatheringCodeActionTitle : '',
		editGatheringCode : {},

		approvalFlag : false,
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		var state = getQueryString('state');
		if (state == '2') {
			that.state = state;
		}
		var todoId = getQueryString('todoId');
		if (todoId != null) {
			that.showAuditGatheringCodeModal(todoId);
		}

		that.loadGatheringCodeStateDictItem();
		that.loadGatheringChannelDictItem();
		that.initTable();

		$('.gathering-code-pic').on('fileuploaded', function(event, data, previewId, index) {
			that.editGatheringCode.storageId = data.response.data.join(',');
			that.addOrUpdateGatheringCodeInner();
		});
	},
	methods : {

		loadGatheringCodeStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'gatheringCodeState'
				}
			}).then(function(res) {
				this.gatheringCodeStateDictItems = res.body.data;
			});
		},

		loadGatheringChannelDictItem : function() {
			var that = this;
			that.$http.get('/gatheringChannel/findAllGatheringChannel').then(function(res) {
				that.gatheringChannelDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.gathering-code-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/gatheringCode/findGatheringCodeByPage',
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
						state : that.state,
						gatheringChannelId : that.gatheringChannelId,
						payee : that.payee,
						inviterUserName : that.inviterUserName,
						userName : that.userName
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
					title : '状态',
					formatter : function(value, row, index, field) {
						if (row.state == '1') {
							return row.stateName;
						}
						return row.auditTypeName + '-' + row.stateName;
					}
				}
				, {
					title : '通道/所属账号/邀请人',
					formatter : function(value, row, index, field) {
						return row.gatheringChannelName + '/' + row.userName + '/' + row.inviterUserName;
					}
				}, {
					title : '详细信息',
					formatter : function(value, row, index, field) {
						if (row.gatheringChannelCode == 'bankCard') {
							var text = '<p>银行/开户人:' + row.openAccountBank + '/' + row.accountHolder + '</p>';
							var text = text + '<p>卡号:' + row.bankCardAccount + '</p>';
							return text;
						} else if (row.gatheringChannelCode == 'alipayCard') {
							var text = '<p>银行/开户人:' + row.openAccountBank + '/' + row.accountHolder + '</p>';
							var text = text + '<p>银行编号/cardId:' + row.bankShortName + '/' + row.bankCardAccount + '</p>';
							return text;
						} else if (row.gatheringChannelCode == 'wechat') {
							return '收款人:' + row.payee;
						} else if (row.gatheringChannelCode == 'alipay') {
							/*if(row.alipayUserid != null){
								var text = '<p>收款人:' + row.payee + '</p>';
								var text = text + '<p>userid:' + row.alipayUserid + '</p>';
								return text;
							}else{
								return '收款人:' + row.payee;
							}*/
							console.log('<p>收款人:' + row.payee+'</p>' + '<p>支付宝id:' + row.alipayUserid + '</p>')
							return '<p>收款人:' + row.payee+'</p>' + '<p>支付宝账号:' + row.account + '</p>'+'<p>姓名:'+row.realName+'</p>';
						} else if (row.gatheringChannelCode == 'wechatMobile') {
							return '手机号/姓名:' + row.mobile + '/' + row.realName;
						} else if (row.gatheringChannelCode == 'alipayIdTransfer' || row.gatheringChannelCode == 'alipayIdXqd') {
							return '<p>账号:' + row.account + '</p>' + '<p>支付宝id:' + row.alipayUserid + '</p>';
						}else if (row.gatheringChannelCode == 'alipayhb'){
							return '<p>群昵称:' + row.groupNickName + '</p>';
						}
					}
				}, {
					field : 'createTime',
					title : '创建时间'
				}, {
					title : '累计收款金额/收款次数/接单次数/成功率',
					formatter : function(value, row, index, field) {
						return row.totalTradeAmount + that.global.systemSetting.currencyUnit + '/' + row.totalPaidOrderNum + '次' + '/' + row.totalOrderNum + '次' + '/<span style="color: green;">' + row.totalSuccessRate + '%</span>';
					}
				}, {
					title : '今日收款金额/收款次数/接单次数/成功率',
					formatter : function(value, row, index, field) {
						return row.todayTradeAmount + that.global.systemSetting.currencyUnit + '/' + row.todayPaidOrderNum + '次' + '/' + row.todayOrderNum + '次' + '/<span style="color: green;">' + row.todaySuccessRate + '%</span>';
					}
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						//var btns = [ '<button type="button" class="edit-gathering-code-btn btn btn-outline-success btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-gathering-code-btn btn btn-outline-danger btn-sm">删除</button>' ];
						var btns = ['<button type="button" class="del-gathering-code-btn btn btn-outline-danger btn-sm">删除</button>' ];
						if (row.gatheringChannelCode == 'wechat' || row.gatheringChannelCode == 'alipay'|| row.gatheringChannelCode=='alipayhb') {
							btns.unshift('<button type="button" class="view-gathering-code-btn btn btn-outline-secondary btn-sm" style="margin-right: 4px;">查看二维码</button>');
						}
						if (row.state == '1') {
							if (row.inUse) {
								btns.unshift('<button type="button" class="offline-btn btn btn-outline-info btn-sm" style="margin-right: 4px;">下码</button>');
							} else {
								btns.unshift('<button type="button" class="online-btn btn btn-outline-info btn-sm" style="margin-right: 4px;">上码</button>');
							}
						}
						if (row.state == '2') {
							btns.unshift('<button type="button" class="audit-gathering-code-btn btn btn-outline-info btn-sm" style="margin-right: 4px;">审核</button>');
						}
						return btns.join('');
					},
					events : {
						'click .audit-gathering-code-btn' : function(event, value, row, index) {
							that.showAuditGatheringCodeModal(row.id);
						},
						'click .online-btn' : function(event, value, row, index) {
							that.updateInUseFlag(row.id, true);
						},
						'click .offline-btn' : function(event, value, row, index) {
							that.updateInUseFlag(row.id, false);
						},
						'click .view-gathering-code-btn' : function(event, value, row, index) {
							that.viewImage('/storage/fetch/' + row.storageId);
						},
						'click .edit-gathering-code-btn' : function(event, value, row, index) {
							that.openEditGatheringCodeModal(row.id);
						},
						'click .del-gathering-code-btn' : function(event, value, row, index) {
							that.delGatheringCode(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.gathering-code-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		showAuditGatheringCodeModal : function(id) {
			var that = this;
			that.$http.get('/gatheringCode/findGatheringCodeById', {
				params : {
					id : id,
				}
			}).then(function(res) {
				that.approvalFlag = true;
				that.editGatheringCode = res.body.data;
			});
		},

		auditDel : function(id) {
			this.approvalFlag = false;
			this.delGatheringCode(this.editGatheringCode.id);
		},

		updateToNormalState : function() {
			var that = this;
			that.$http.get('/gatheringCode/updateToNormalState', {
				params : {
					id : that.editGatheringCode.id
				}
			}).then(function(res) {
				that.approvalFlag = false;
				that.refreshTable();
			});
		},

		initFileUploadWidget : function(storageId) {
			var initialPreview = [];
			var initialPreviewConfig = [];
			if (storageId != null) {
				initialPreview.push('/storage/fetch/' + storageId);
				initialPreviewConfig.push({
					downloadUrl : '/storage/fetch/' + storageId
				});
			}
			$('.gathering-code-pic').fileinput('destroy').fileinput({
				browseOnZoneClick : true,
				showBrowse : false,
				showCaption : false,
				showClose : true,
				showRemove : false,
				showUpload : false,
				dropZoneTitle : '点击选择图片',
				dropZoneClickTitle : '',
				layoutTemplates : {
					footer : ''
				},
				maxFileCount : 1,
				uploadUrl : '/storage/uploadPic',
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : initialPreview,
				initialPreviewAsData : true,
				initialPreviewConfig : initialPreviewConfig
			});
		},

		updateInUseFlag : function(id, inUse) {
			var that = this;
			that.$http.get('/gatheringCode/updateInUseFlag', {
				params : {
					id : id,
					inUse : inUse
				}
			}).then(function(res) {
				that.refreshTable();
			});
		},

		viewImage : function(imagePath) {
			var image = new Image();
			image.src = imagePath;
			var viewer = new Viewer(image, {
				hidden : function() {
					viewer.destroy();
				},
			});
			viewer.show();
		},

		openAddGatheringCodeModal : function() {
			this.addOrUpdateGatheringCodeFlag = true;
			this.gatheringCodeActionTitle = '新增收款码';
			this.editGatheringCode = {
				userName : '',
				inviterUserName : '',
				gatheringChannelId : '',
				state : '',
				fixedGatheringAmount : true,
				gatheringAmount : '',
				payee : ''
			};
			this.initFileUploadWidget();
		},

		openEditGatheringCodeModal : function(gatheringCodeId) {
			var that = this;
			that.$http.get('/gatheringCode/findGatheringCodeById', {
				params : {
					id : gatheringCodeId,
				}
			}).then(function(res) {
				that.addOrUpdateGatheringCodeFlag = true;
				that.gatheringCodeActionTitle = '编辑收款码';
				that.editGatheringCode = res.body.data;
				that.initFileUploadWidget(res.body.data.storageId);
			});
		},

		addOrUpdateGatheringCode : function() {
			var that = this;
			var editGatheringCode = that.editGatheringCode;
			if (editGatheringCode.userName == null || editGatheringCode.userName == '') {
				layer.alert('请输入所属账号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGatheringCode.gatheringChannelId === null || editGatheringCode.gatheringChannelId === '') {
				layer.alert('请选择收款通道', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGatheringCode.payee == null || editGatheringCode.payee == '') {
				layer.alert('请输入收款人', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (!that.global.receiveOrderSetting.unfixedGatheringCodeReceiveOrder) {
				if (editGatheringCode.gatheringAmount == null || editGatheringCode.gatheringAmount == '') {
					layer.alert('请输入收款金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}

			if ($('.gathering-code-pic').fileinput('getPreview').content.length != 0) {
				that.addOrUpdateGatheringCodeInner();
			} else {
				var filesCount = $('.gathering-code-pic').fileinput('getFilesCount');
				if (filesCount == 0) {
					layer.alert('请选择要上传的图片', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				$('.gathering-code-pic').fileinput('upload');
			}
		},

		addOrUpdateGatheringCodeInner : function() {
			var that = this;
			that.$http.post('/gatheringCode/addOrUpdateGatheringCode', that.editGatheringCode).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateGatheringCodeFlag = false;
				that.refreshTable();
			});
		},

		delGatheringCode : function(gatheringCodeId) {
			var that = this;
			that.$http.get('/gatheringCode/delGatheringCodeById', {
				params : {
					id : gatheringCodeId,
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateGatheringCodeFlag = false;
				that.refreshTable();
			});
		}
	}
});