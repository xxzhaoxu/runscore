var gatheringCodeVM = new Vue({
	el : '#gatheringCode',
	data : {
		global : GLOBAL,
		auditGatheringCode : false,
		unfixedGatheringCodeReceiveOrder : false,
		gatheringCodeGoogleAuth : false,
		gatheringChannelId : '',
		selectedGatheringChannel : null,
		gatheringChannelDictItems : [],
		accountTime : dayjs().format('YYYY-MM-DD'),
		gatheringCodes : [],
		pageNum : 1,
		totalPage : 1,
		showGatheringCodeFlag : true,

		editGatheringCode : {
			gatheringChannelId : '',
			state : '',
			gatheringAmount : '',
			bankCard : '',
			bankShortName : '',
			googleVerCode : '',
			payee : ''
		},
		showEditGatheringCodeFlag : false,
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		headerVM.title = '收款码';
		headerVM.showBackFlag = true;
		that.loadReceiveOrderSetting();
		that.loadReceiveOrderChannel();

		$('.gathering-code-pic').on('fileuploaded', function(event, data, previewId, index) {
			that.editGatheringCode.storageId = data.response.data.join(',');
			that.addOrUpdateGatheringCodeInner();
		});
	},
	methods : {

		loadReceiveOrderSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getReceiveOrderSetting').then(function(res) {
				that.auditGatheringCode = res.body.data.auditGatheringCode;
				that.unfixedGatheringCodeReceiveOrder = res.body.data.unfixedGatheringCodeReceiveOrder;
				that.gatheringCodeGoogleAuth = res.body.data.gatheringCodeGoogleAuth;
			});
		},

		loadReceiveOrderChannel : function() {
			var that = this;
			that.$http.get('/userAccount/findMyReceiveOrderChannel').then(function(res) {
				that.gatheringChannelDictItems = res.body.data;
				if (that.gatheringChannelDictItems.length > 0) {
					that.switchTab(that.gatheringChannelDictItems[0]);
				}
			});
		},

		/**
		 * 加载收款通道字典项
		 */
		loadGatheringChannelDictItem : function() {
			var that = this;
			that.$http.get('/gatheringChannel/findAllGatheringChannel').then(function(res) {
				that.gatheringChannelDictItems = res.body.data;
				console.log(that.gatheringChannelDictItems)
				if (that.gatheringChannelDictItems.length > 0) {
					that.switchTab(that.gatheringChannelDictItems[0].id);
				}
			});
		},

		switchTab : function(gatheringChannel) {
			this.selectedGatheringChannel = gatheringChannel;
			this.query();
		},

		query : function() {
			this.pageNum = 1;
			this.loadGatheringCodeByPage();
		},

		prePage : function() {
			this.pageNum = this.pageNum - 1;
			this.loadGatheringCodeByPage();
		},

		nextPage : function() {
			this.pageNum = this.pageNum + 1;
			this.loadGatheringCodeByPage();
		},

		loadGatheringCodeByPage : function() {
			var that = this;
			that.$http.get('/gatheringCode/findMyGatheringCodeByPage', {
				params : {
					pageSize : 5,
					pageNum : that.pageNum,
					gatheringChannelId : that.selectedGatheringChannel.channelId
				}
			}).then(function(res) {
				console.log(res.body)
				that.gatheringCodes = res.body.data.content;
				that.pageNum = res.body.data.pageNum;
				that.totalPage = res.body.data.totalPage;
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
				that.hideEditGatheringCodePage();
				that.query();
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

		initFileUploadWidget : function(storageId) {
			var that = this;
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
			}).on('fileuploaded', function(event, data, previewId, index) {
				that.editGatheringCode.storageId = data.response.data.join(',');
				that.addOrUpdateGatheringCodeInner();
			});
		},

		switchGatheringAmountMode : function() {
			if (!this.editGatheringCode.fixedGatheringAmount) {
				this.editGatheringCode.gatheringAmount = '';
			}
		},

		showEditGatheringCodePage : function() {
			var that = this;
			that.editGatheringCode = {
				openAccountBank : '',
				accountHolder : '',
				bankShortName : '',
				bankCardAccount : '',
				gatheringChannelId : that.gatheringChannelId,
				state : '',
				fixedGatheringAmount : true,
				gatheringAmount : '',
				alipayUserid : '',
				mobile : '',
				realName : '',
				account : '',
				googleVerCode : '',
				payee : ''
			};
			that.showEditGatheringCodePageInner();
			that.initFileUploadWidget();
		},

		showEditGatheringCodePageInner : function() {
			headerVM.showBackFlag = false;
			headerVM.title = '新增收款码';
			this.showGatheringCodeFlag = false;
			this.showEditGatheringCodeFlag = true;
		},

		hideEditGatheringCodePage : function() {
			headerVM.showBackFlag = true;
			headerVM.title = '收款码';
			this.showGatheringCodeFlag = true;
			this.showEditGatheringCodeFlag = false;
		},

		addOrUpdateGatheringCode : function() {
			console.log("addOrUpdateGatheringCode")
			var that = this;
			var selectedGatheringChannel = that.selectedGatheringChannel;
			var editGatheringCode = that.editGatheringCode;
			if (selectedGatheringChannel === null) {
				layer.alert('请选择收款通道', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}


			editGatheringCode.gatheringChannelId = selectedGatheringChannel.channelId;
			if (selectedGatheringChannel.channelCode == 'bankCard' ) {
				if (editGatheringCode.payee == null || editGatheringCode.payee == '') {
					layer.alert('请选择收款人', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.openAccountBank == null || editGatheringCode.openAccountBank == '') {
					layer.alert('请输入银行', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.accountHolder == null || editGatheringCode.accountHolder == '') {
					layer.alert('请输入开户人', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.bankCardAccount == null || editGatheringCode.bankCardAccount == '') {
					layer.alert('请输入卡号', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (!that.unfixedGatheringCodeReceiveOrder) {
					if (editGatheringCode.gatheringAmount == null || editGatheringCode.gatheringAmount == '') {
						layer.alert('请输入收款金额', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				if (that.gatheringCodeGoogleAuth) {
					if (editGatheringCode.googleVerCode == null || editGatheringCode.googleVerCode == '') {
						layer.alert('请输入谷歌验证码', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				that.addOrUpdateGatheringCodeInner();
			} else if (selectedGatheringChannel.channelCode == 'alipayCard') {
				if (editGatheringCode.payee == null || editGatheringCode.payee == '') {
					layer.alert('请选择收款人', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.openAccountBank == null || editGatheringCode.openAccountBank == '') {
					layer.alert('请输入银行', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.bankShortName == null || editGatheringCode.bankShortName == '') {
					layer.alert('请输入银行编号大写', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.accountHolder == null || editGatheringCode.accountHolder == '') {
					layer.alert('请输入开户人', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.bankCardAccount == null || editGatheringCode.bankCardAccount == '') {
					layer.alert('请输入cardId', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (!that.unfixedGatheringCodeReceiveOrder) {
					if (editGatheringCode.gatheringAmount == null || editGatheringCode.gatheringAmount == '') {
						layer.alert('请输入收款金额', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				if (that.gatheringCodeGoogleAuth) {
					if (editGatheringCode.googleVerCode == null || editGatheringCode.googleVerCode == '') {
						layer.alert('请输入谷歌验证码', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				that.addOrUpdateGatheringCodeInner();
			} else if (selectedGatheringChannel.channelCode == 'wechat' || selectedGatheringChannel.channelCode == 'alipay' || selectedGatheringChannel.channelCode == 'ysf'|| selectedGatheringChannel.channelCode =='alipayhb') {
				if (editGatheringCode.payee == null || editGatheringCode.payee == '') {
					layer.alert('请选择收款人', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (!that.unfixedGatheringCodeReceiveOrder) {
					if (editGatheringCode.gatheringAmount == null || editGatheringCode.gatheringAmount == '') {
						layer.alert('请输入收款金额', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				if (that.gatheringCodeGoogleAuth) {
					if (editGatheringCode.googleVerCode == null || editGatheringCode.googleVerCode == '') {
						layer.alert('请输入谷歌验证码', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				var filesCount = $('.gathering-code-pic').fileinput('getFilesCount');
				if (filesCount == 0) {
					layer.alert('请选择要上传的二维码', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				$('.gathering-code-pic').fileinput('upload');
			} else if (selectedGatheringChannel.channelCode == 'wechatMobile') {
				if (editGatheringCode.mobile == null || editGatheringCode.mobile == '') {
					layer.alert('请输入手机号', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.realName == null || editGatheringCode.realName == '') {
					layer.alert('请输入姓名', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (!that.unfixedGatheringCodeReceiveOrder) {
					if (editGatheringCode.gatheringAmount == null || editGatheringCode.gatheringAmount == '') {
						layer.alert('请输入收款金额', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				if (that.gatheringCodeGoogleAuth) {
					if (editGatheringCode.googleVerCode == null || editGatheringCode.googleVerCode == '') {
						layer.alert('请输入谷歌验证码', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				that.addOrUpdateGatheringCodeInner();
			} else if (selectedGatheringChannel.channelCode == 'alipayTransfer') {
				if (editGatheringCode.payee == null || editGatheringCode.payee == '') {
					layer.alert('请选择收款人', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.account == null || editGatheringCode.account == '') {
					layer.alert('请输入账号', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.realName == null || editGatheringCode.realName == '') {
					layer.alert('请输入姓名', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (!that.unfixedGatheringCodeReceiveOrder) {
					if (editGatheringCode.gatheringAmount == null || editGatheringCode.gatheringAmount == '') {
						layer.alert('请输入收款金额', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				if (that.gatheringCodeGoogleAuth) {
					if (editGatheringCode.googleVerCode == null || editGatheringCode.googleVerCode == '') {
						layer.alert('请输入谷歌验证码', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
				that.addOrUpdateGatheringCodeInner();
			}else if (selectedGatheringChannel.channelCode == 'alipayIdTransfer' || selectedGatheringChannel.channelCode == 'alipayIdXqd') {
				if (editGatheringCode.account == null || editGatheringCode.account == '') {
					layer.alert('请输入账号', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editGatheringCode.alipayUserid == null || editGatheringCode.alipayUserid == '') {
					layer.alert('请输入支付宝id', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				editGatheringCode.alipayUserid = editGatheringCode.alipayUserid.replace(/\s+/g, '');
				that.addOrUpdateGatheringCodeInner();
			}else if (selectedGatheringChannel.channelCode == 'alipayhb1'){//支付宝红包
				if (editGatheringCode.payee == null || editGatheringCode.payee == '') {
					layer.alert('请选择收款人', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}

				var filesCount = $('.gathering-code-pic').fileinput('getFilesCount');
				if (filesCount == 0) {
					layer.alert('请选择要上传的二维码', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				console.log("----------------------")
				$('.gathering-code-pic').fileinput('upload');
				// that.addOrUpdateGatheringCodeInner();
			}
		},

		addOrUpdateGatheringCodeInner : function() {
			var that = this;

			console.log(that.editGatheringCode)
			that.$http.post('/gatheringCode/addGatheringCode', that.editGatheringCode).then(function(res) {
				var msg = '操作成功!';
				if (that.auditGatheringCode) {
					msg = '操作成功,已通知系统进行审核!';
				}
				layer.alert(msg, {
					icon : 1,
					time : 3000,
					shade : false
				});
				//that.hideEditGatheringCodePage();
				//that.query();
				window.location.reload();
			});
		},

		delGatheringCode : function(gatheringCodeId) {
			var that = this;
			that.$http.get('/gatheringCode/delMyGatheringCodeById', {
				params : {
					id : gatheringCodeId,
				}
			}).then(function(res) {
				var msg = '操作成功!';
				if (that.auditGatheringCode) {
					msg = '操作成功,已通知系统进行审核!';
				}
				layer.alert(msg, {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.hideEditGatheringCodePage();
				that.query();
			});
		}
	}
});