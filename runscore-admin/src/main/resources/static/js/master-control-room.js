var masterControlRoomVM = new Vue({
	el : '#master-control-room',
	data : {
		/**
		 * 系统设置start
		 */
		websiteTitle : '',
		payTechnicalSupport : '',
		currencyUnit : '',
		singleDeviceLoginFlag : false,
		memberMeEnabled : false,
		showRankingListFlag : false,
		mobileLoginGoogleAuth : false,
		backgroundLoginGoogleAuth : false,
		showCodeTailoring : false,

		/**
		 * 注册设置start
		 */
		registerEnabled : '',
		inviteCodeEffectiveDuration : '',
		appMonitorUrl : '',
		inviteRegisterEnabled : false,
		onlyOpenMemberAccount : false,
		onlyOpenAgentAccount : false,
		onlyLevelRebate : false,
		onlyLevelNum : '',
		agentExplain : '',

		/**
		 * 商户订单start
		 */
		receiveOrderEffectiveDuration : '',
		orderPayEffectiveDuration : '',
		receiveOrderUpperLimit : '',
		receiveOrderInterval : '',
		receiveOrderAppealInterval : '',
		showAllOrder : false,
		cashDepositMinimumRequire : '',
		cashPledge : '',
		unfixedGatheringCodeReceiveOrder : false,
		gatheringCodeGoogleAuth : false,
		stopStartAndReceiveOrder : false,
		banReceiveRepeatOrder : false,
		banReceiveCodeOrder : false,
		receiveOrderSound : false,
		auditGatheringCode : false,
		openAutoReceiveOrder : false,
		freezeModelEnabled : false,
		freezeEffectiveDuration : '',
		unconfirmedAutoFreezeDuration : '',
		noOpsStopReceiveOrder : '',
		dispatchMode : false,
		sameCityPriority : false,
		gatheringCodeEverydayUsedUpperLimit : false,
		gatheringCodeUsedUpperLimit : '',

		/**
		 * 充值start
		 */
		rechargeOrderEffectiveDuration : '',
		rechargeReturnWaterRate : '',
		rechargeLowerLimit : '',
		rechargeUpperLimit : '',
		rechargeReturnWaterRateEnabled : false,
		quickInputAmount : '',
		rechargeExplain : '',
		cantContinuousSubmit : false,

		/**
		 * 提现start
		 */
		everydayWithdrawTimesUpperLimit : '',
		withdrawLowerLimit : '',
		withdrawUpperLimit : '',
		withdrawExplain : '',

		/**
		 * 客服二维码start
		 */
		qrcodeStorageId : '',
		customerServiceExplain : '',
		customerServiceUrl : '',
		uploadQrcodeFlag : false,

		/**
		 * 刷新缓存start
		 */
		refreshConfigItem : true,
		refreshDict : true
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.loadSystemSetting();
		that.loadRegisterSetting();
		that.loadReceiveOrderSetting();
		that.loadRechargeSetting();
		that.loadWithdrawSetting();
		that.loadCustomerQrcodeSetting();

		$('.qrcode-pic').on('filebatchuploadsuccess', function(event, data) {
			that.qrcodeStorageId = data.response.data.join(',');
			that.uploadQrcodeFlag = false;
		});
	},
	methods : {

		loadSystemSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getSystemSetting').then(function(res) {
				if (res.body.data != null) {
					that.websiteTitle = res.body.data.websiteTitle;
					that.payTechnicalSupport = res.body.data.payTechnicalSupport;
					that.currencyUnit = res.body.data.currencyUnit;
					that.singleDeviceLoginFlag = res.body.data.singleDeviceLoginFlag;
					that.memberMeEnabled = res.body.data.memberMeEnabled;
					that.showRankingListFlag = res.body.data.showRankingListFlag;
					that.showCodeTailoring = res.body.data.showCodeTailoring;
					that.mobileLoginGoogleAuth = res.body.data.mobileLoginGoogleAuth;
					that.backgroundLoginGoogleAuth = res.body.data.backgroundLoginGoogleAuth;
				}
			});
		},

		updateSystemSetting : function() {
			var that = this;
			var websiteTitle = that.websiteTitle;
			if (websiteTitle === null || websiteTitle === '') {
				layer.alert('请输入网站标题', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var payTechnicalSupport = that.payTechnicalSupport;
			if (payTechnicalSupport === null || payTechnicalSupport === '') {
				layer.alert('请输入支付技术支持', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var currencyUnit = that.currencyUnit;
			if (currencyUnit === null || currencyUnit === '') {
				layer.alert('请输入货币单位', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var singleDeviceLoginFlag = that.singleDeviceLoginFlag;
			if (singleDeviceLoginFlag === null) {
				layer.alert('请设置是否限制单一设备登录', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var memberMeEnabled = that.memberMeEnabled;
			if (memberMeEnabled === null) {
				layer.alert('请设置是否开启记住密码功能', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var showRankingListFlag = that.showRankingListFlag;
			if (showRankingListFlag === null) {
				layer.alert('请设置是否显示排行榜', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var showCodeTailoring = that.showCodeTailoring;
			if (showCodeTailoring === null) {
				layer.alert('请设置是否开启收款码裁剪', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var mobileLoginGoogleAuth = that.mobileLoginGoogleAuth;
			if (mobileLoginGoogleAuth === null) {
				layer.alert('是否开启会员端登陆启用谷歌身份验证', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var backgroundLoginGoogleAuth = that.backgroundLoginGoogleAuth;
			if (backgroundLoginGoogleAuth === null) {
				layer.alert('是否开启后台管理登陆启用谷歌身份验证', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateSystemSetting', {
				websiteTitle : websiteTitle,
				payTechnicalSupport : payTechnicalSupport,
				currencyUnit : currencyUnit,
				singleDeviceLoginFlag : singleDeviceLoginFlag,
				memberMeEnabled : memberMeEnabled,
				showCodeTailoring : showCodeTailoring,
				mobileLoginGoogleAuth : mobileLoginGoogleAuth,
				backgroundLoginGoogleAuth : backgroundLoginGoogleAuth,
				showRankingListFlag : showRankingListFlag
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadSystemSetting();
			});
		},

		loadRegisterSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getRegisterSetting').then(function(res) {
				if (res.body.data != null) {
					that.registerEnabled = res.body.data.registerEnabled;
					that.inviteCodeEffectiveDuration = res.body.data.inviteCodeEffectiveDuration;
					that.appMonitorUrl = res.body.data.appMonitorUrl;
					that.inviteRegisterEnabled = res.body.data.inviteRegisterEnabled;
					that.onlyOpenMemberAccount = res.body.data.onlyOpenMemberAccount;
					that.onlyOpenAgentAccount = res.body.data.onlyOpenAgentAccount;
					that.onlyLevelRebate = res.body.data.onlyLevelRebate;
					that.onlyLevelNum = res.body.data.onlyLevelNum;
					that.agentExplain = res.body.data.agentExplain;
				}
			});
		},

		updateRegisterSetting : function() {
			var that = this;
			var registerEnabled = that.registerEnabled;
			if (registerEnabled === null) {
				layer.alert('请设置是否开放注册功能', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var inviteCodeEffectiveDuration = that.inviteCodeEffectiveDuration;
			if (inviteCodeEffectiveDuration === null || inviteCodeEffectiveDuration === '') {
				layer.alert('请输入邀请码有效时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var appMonitorUrl = that.appMonitorUrl;
			if (appMonitorUrl === null || appMonitorUrl === '') {
				layer.alert('请输入APP监控下载地址', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var inviteRegisterEnabled = that.inviteRegisterEnabled;
			if (inviteRegisterEnabled === null) {
				layer.alert('请选择是否启用邀请码注册模式', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var onlyOpenMemberAccount = that.onlyOpenMemberAccount;
			if (onlyOpenMemberAccount === null) {
				layer.alert('请选择是否开启下级开户能开会员账号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var onlyOpenAgentAccount = that.onlyOpenAgentAccount;
			if (onlyOpenAgentAccount === null) {
				layer.alert('请选择是否开启下级开户能开代理账号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var onlyLevelRebate = that.onlyLevelRebate;
			if (onlyLevelRebate === null) {
				layer.alert('请选择是否开启限制多少级返佣模式', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var onlyLevelNum = that.onlyLevelNum;
			if (onlyLevelNum === null) {
				layer.alert('请选择限制几级返佣', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var agentExplain = that.agentExplain;
			if (agentExplain === null || agentExplain === '') {
				layer.alert('请输入代理说明', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/masterControl/updateRegisterSetting', {
				registerEnabled : registerEnabled,
				inviteCodeEffectiveDuration : inviteCodeEffectiveDuration,
				appMonitorUrl : appMonitorUrl,
				inviteRegisterEnabled : inviteRegisterEnabled,
				onlyOpenMemberAccount : onlyOpenMemberAccount,
				onlyOpenAgentAccount : onlyOpenAgentAccount,
				onlyLevelRebate : onlyLevelRebate,
				onlyLevelNum : onlyLevelNum,
				agentExplain : agentExplain
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadRegisterSetting();
			});
		},

		loadReceiveOrderSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getReceiveOrderSetting').then(function(res) {
				if (res.body.data != null) {
					that.stopStartAndReceiveOrder = res.body.data.stopStartAndReceiveOrder;
					that.receiveOrderEffectiveDuration = res.body.data.receiveOrderEffectiveDuration;
					that.orderPayEffectiveDuration = res.body.data.orderPayEffectiveDuration;
					that.receiveOrderInterval = res.body.data.receiveOrderInterval;
					that.receiveOrderAppealInterval = res.body.data.receiveOrderAppealInterval;
					that.receiveOrderUpperLimit = res.body.data.receiveOrderUpperLimit;
					that.showAllOrder = res.body.data.showAllOrder;
					that.cashDepositMinimumRequire = res.body.data.cashDepositMinimumRequire;
					that.cashPledge = res.body.data.cashPledge;
					that.unfixedGatheringCodeReceiveOrder = res.body.data.unfixedGatheringCodeReceiveOrder;
					that.gatheringCodeGoogleAuth = res.body.data.gatheringCodeGoogleAuth;
					that.banReceiveRepeatOrder = res.body.data.banReceiveRepeatOrder;
					that.banReceiveCodeOrder = res.body.data.banReceiveCodeOrder;
					that.receiveOrderSound = res.body.data.receiveOrderSound;
					that.auditGatheringCode = res.body.data.auditGatheringCode;
					that.openAutoReceiveOrder = res.body.data.openAutoReceiveOrder;
					that.freezeModelEnabled = res.body.data.freezeModelEnabled;
					that.freezeEffectiveDuration = res.body.data.freezeEffectiveDuration;
					that.unconfirmedAutoFreezeDuration = res.body.data.unconfirmedAutoFreezeDuration;
					that.noOpsStopReceiveOrder = res.body.data.noOpsStopReceiveOrder;
					that.dispatchMode = res.body.data.dispatchMode;
					that.sameCityPriority = res.body.data.sameCityPriority;
					that.gatheringCodeEverydayUsedUpperLimit = res.body.data.gatheringCodeEverydayUsedUpperLimit;
					that.gatheringCodeUsedUpperLimit = res.body.data.gatheringCodeUsedUpperLimit;
				}
			});
		},

		updateReceiveOrderSetting : function() {
			var that = this;
			var stopStartAndReceiveOrder = that.stopStartAndReceiveOrder;
			if (stopStartAndReceiveOrder === null) {
				layer.alert('请选择是否暂停发单接单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var banReceiveRepeatOrder = that.banReceiveRepeatOrder;
			if (banReceiveRepeatOrder === null) {
				layer.alert('请选择同一收款方式禁止接相同金额的订单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var banReceiveCodeOrder = that.banReceiveCodeOrder;
			if (banReceiveCodeOrder === null) {
				layer.alert('请选择同一码禁止接相同金额的订单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var receiveOrderEffectiveDuration = that.receiveOrderEffectiveDuration;
			if (receiveOrderEffectiveDuration === null || receiveOrderEffectiveDuration === '') {
				layer.alert('请输入接单有效时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var orderPayEffectiveDuration = that.orderPayEffectiveDuration;
			if (orderPayEffectiveDuration === null || orderPayEffectiveDuration === '') {
				layer.alert('请输入支付有效时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var receiveOrderInterval = that.receiveOrderInterval;
			if (receiveOrderInterval === null || receiveOrderInterval === '') {
				layer.alert('请输入接单间隔', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var receiveOrderAppealInterval = that.receiveOrderAppealInterval;
			if (receiveOrderAppealInterval === null || receiveOrderAppealInterval === '') {
				layer.alert('请输入申诉间隔', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var receiveOrderUpperLimit = that.receiveOrderUpperLimit;
			if (receiveOrderUpperLimit === null || receiveOrderUpperLimit === '') {
				layer.alert('请输入接单数量上限', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var showAllOrder = that.showAllOrder;
			if (showAllOrder === null) {
				layer.alert('请选择是否显示所有订单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var cashDepositMinimumRequire = that.cashDepositMinimumRequire;
			if (cashDepositMinimumRequire === null || cashDepositMinimumRequire === '') {
				layer.alert('请输入账户余额最低要求金额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var cashPledge = that.cashPledge;
			if (cashPledge === null || cashPledge === '') {
				layer.alert('请输入押金', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var unfixedGatheringCodeReceiveOrder = that.unfixedGatheringCodeReceiveOrder;
			if (unfixedGatheringCodeReceiveOrder === null) {
				layer.alert('请选择是否支持不固定金额收款码接单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var gatheringCodeGoogleAuth = that.gatheringCodeGoogleAuth;
			if (gatheringCodeGoogleAuth === null) {
				layer.alert('请选择是否开启新增收款码填写谷歌验证码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var receiveOrderSound = that.receiveOrderSound;
			if (receiveOrderSound === null) {
				layer.alert('请选择是否开启接单提示音', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var auditGatheringCode = that.auditGatheringCode;
			if (auditGatheringCode === null) {
				layer.alert('请设置是否审核收款码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var openAutoReceiveOrder = that.openAutoReceiveOrder;
			if (openAutoReceiveOrder === null) {
				layer.alert('请设置是否开放自动接单功能', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var freezeModelEnabled = that.freezeModelEnabled;
			if (freezeModelEnabled === null) {
				layer.alert('请设置是否启用冻结模式', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var freezeEffectiveDuration = that.freezeEffectiveDuration;
			if (freezeEffectiveDuration === null || freezeEffectiveDuration === '') {
				layer.alert('请输入冻结时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var unconfirmedAutoFreezeDuration = that.unconfirmedAutoFreezeDuration;
			if (unconfirmedAutoFreezeDuration === null || unconfirmedAutoFreezeDuration === '') {
				layer.alert('请输入超过多少分钟未确认冻结该订单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var noOpsStopReceiveOrder = that.noOpsStopReceiveOrder;
			if (noOpsStopReceiveOrder === null || noOpsStopReceiveOrder === '') {
				layer.alert('请输入超过多少分钟无操作停止接单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var dispatchMode = that.dispatchMode;
			if (dispatchMode === null) {
				layer.alert('请设置是否启用派单模式', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var sameCityPriority = that.sameCityPriority;
			if (sameCityPriority === null) {
				layer.alert('请设置是否启用同城匹配', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var gatheringCodeEverydayUsedUpperLimit = that.gatheringCodeEverydayUsedUpperLimit;
			if (gatheringCodeEverydayUsedUpperLimit === null) {
				layer.alert('请设置是否限制收款码每日收款次数', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var gatheringCodeUsedUpperLimit = that.gatheringCodeUsedUpperLimit;
			if (gatheringCodeUsedUpperLimit === null || gatheringCodeUsedUpperLimit === '') {
				layer.alert('请输入收款码收款次数上限数量', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateReceiveOrderSetting', {
				receiveOrderEffectiveDuration : receiveOrderEffectiveDuration,
				orderPayEffectiveDuration : orderPayEffectiveDuration,
				receiveOrderUpperLimit : receiveOrderUpperLimit,
				receiveOrderInterval : receiveOrderInterval,
				receiveOrderAppealInterval : receiveOrderAppealInterval,
				showAllOrder : showAllOrder,
				cashDepositMinimumRequire : cashDepositMinimumRequire,
				cashPledge : cashPledge,
				unfixedGatheringCodeReceiveOrder : unfixedGatheringCodeReceiveOrder,
				gatheringCodeGoogleAuth : gatheringCodeGoogleAuth,
				stopStartAndReceiveOrder : stopStartAndReceiveOrder,
				banReceiveRepeatOrder : banReceiveRepeatOrder,
				banReceiveCodeOrder : banReceiveCodeOrder,
				receiveOrderSound : receiveOrderSound,
				auditGatheringCode : auditGatheringCode,
				freezeModelEnabled : freezeModelEnabled,
				freezeEffectiveDuration : freezeEffectiveDuration,
				unconfirmedAutoFreezeDuration : unconfirmedAutoFreezeDuration,
				openAutoReceiveOrder : openAutoReceiveOrder,
				noOpsStopReceiveOrder : noOpsStopReceiveOrder,
				dispatchMode : dispatchMode,
				sameCityPriority : sameCityPriority,
				gatheringCodeEverydayUsedUpperLimit : gatheringCodeEverydayUsedUpperLimit,
				gatheringCodeUsedUpperLimit : gatheringCodeUsedUpperLimit
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadReceiveOrderSetting();
			});
		},

		loadRechargeSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getRechargeSetting').then(function(res) {
				if (res.body.data != null) {
					that.rechargeOrderEffectiveDuration = res.body.data.orderEffectiveDuration;
					that.rechargeReturnWaterRate = res.body.data.returnWaterRate;
					that.rechargeLowerLimit = res.body.data.rechargeLowerLimit;
					that.rechargeUpperLimit = res.body.data.rechargeUpperLimit;
					that.rechargeReturnWaterRateEnabled = res.body.data.returnWaterRateEnabled;
					that.quickInputAmount = res.body.data.quickInputAmount;
					that.rechargeExplain = res.body.data.rechargeExplain;
					that.cantContinuousSubmit = res.body.data.cantContinuousSubmit;
				}
			});
		},

		updateRechargeSetting : function() {
			var that = this;
			var rechargeOrderEffectiveDuration = that.rechargeOrderEffectiveDuration;
			if (rechargeOrderEffectiveDuration === null || rechargeOrderEffectiveDuration === '') {
				layer.alert('请输入充值订单有效时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rechargeReturnWaterRate = that.rechargeReturnWaterRate;
			if (rechargeReturnWaterRate === null || rechargeReturnWaterRate === '') {
				layer.alert('请输入充值返水率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rechargeReturnWaterRateEnabled = that.rechargeReturnWaterRateEnabled;
			if (rechargeReturnWaterRateEnabled === null) {
				layer.alert('请选择是否启用', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rechargeLowerLimit = that.rechargeLowerLimit;
			if (rechargeLowerLimit === null || rechargeLowerLimit === '') {
				layer.alert('请输入充值最低限额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rechargeUpperLimit = that.rechargeUpperLimit;
			if (rechargeUpperLimit === null || rechargeUpperLimit === '') {
				layer.alert('请输入充值最高限额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var quickInputAmount = that.quickInputAmount;
			if (quickInputAmount === null || quickInputAmount === '') {
				layer.alert('请输入快速设置金额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rechargeExplain = that.rechargeExplain;
			if (rechargeExplain === null || rechargeExplain === '') {
				layer.alert('请输入充值说明', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var cantContinuousSubmit = that.cantContinuousSubmit;
			if (cantContinuousSubmit === null) {
				layer.alert('请选择是否限制不能连续提交充值订单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateRechargeSetting', {
				orderEffectiveDuration : rechargeOrderEffectiveDuration,
				returnWaterRate : rechargeReturnWaterRate,
				rechargeLowerLimit : rechargeLowerLimit,
				rechargeUpperLimit : rechargeUpperLimit,
				returnWaterRateEnabled : rechargeReturnWaterRateEnabled,
				quickInputAmount : quickInputAmount,
				rechargeExplain : rechargeExplain,
				cantContinuousSubmit : cantContinuousSubmit
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadRechargeSetting();
			});
		},

		loadWithdrawSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getWithdrawSetting').then(function(res) {
				if (res.body.data != null) {
					that.everydayWithdrawTimesUpperLimit = res.body.data.everydayWithdrawTimesUpperLimit;
					that.withdrawLowerLimit = res.body.data.withdrawLowerLimit;
					that.withdrawUpperLimit = res.body.data.withdrawUpperLimit;
					that.withdrawExplain = res.body.data.withdrawExplain;
				}
			});
		},

		updateWithdrawSetting : function() {
			var that = this;
			var everydayWithdrawTimesUpperLimit = that.everydayWithdrawTimesUpperLimit;
			if (everydayWithdrawTimesUpperLimit === null || everydayWithdrawTimesUpperLimit === '') {
				layer.alert('请输入每日提现次数上限', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var withdrawLowerLimit = that.withdrawLowerLimit;
			if (withdrawLowerLimit === null || withdrawLowerLimit === '') {
				layer.alert('请输入提现最低限额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var withdrawUpperLimit = that.withdrawUpperLimit;
			if (withdrawUpperLimit === null || withdrawUpperLimit === '') {
				layer.alert('请输入提现最高限额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var withdrawExplain = that.withdrawExplain;
			if (withdrawExplain === null || withdrawExplain === '') {
				layer.alert('请输入提现说明', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateWithdrawSetting', {
				everydayWithdrawTimesUpperLimit : everydayWithdrawTimesUpperLimit,
				withdrawLowerLimit : withdrawLowerLimit,
				withdrawUpperLimit : withdrawUpperLimit,
				withdrawExplain : withdrawExplain
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadWithdrawSetting();
			});
		},

		loadCustomerQrcodeSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getCustomerQrcodeSetting').then(function(res) {
				if (res.body.data != null) {
					that.customerServiceExplain = res.body.data.customerServiceExplain;
					that.customerServiceUrl = res.body.data.customerServiceUrl;
					that.qrcodeStorageId = res.body.data.qrcodeStorageId;
				}
			});
		},

		showUploadQrcodeModal : function() {
			this.uploadQrcodeFlag = true;
			$('.qrcode-pic').fileinput('destroy').fileinput({
				uploadAsync : false,
				browseOnZoneClick : true,
				showBrowse : false,
				showCaption : false,
				showClose : true,
				showRemove : false,
				showUpload : false,
				dropZoneTitle : '点击选择二维码',
				dropZoneClickTitle : '',
				layoutTemplates : {
					footer : ''
				},
				maxFileCount : 1,
				uploadUrl : '/storage/uploadPic',
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : [],
				initialPreviewAsData : true,
				initialPreviewConfig : []
			});
		},

		uploadQrcode : function() {
			var filesCount = $('.qrcode-pic').fileinput('getFilesCount');
			if (filesCount == 0) {
				layer.alert('请选择要上传的二维码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			$('.qrcode-pic').fileinput('upload');
		},

		updateCustomerQrcodeSetting : function() {
			var that = this;
			var qrcodeStorageId = that.qrcodeStorageId;
			if (qrcodeStorageId === null || qrcodeStorageId === '') {
				layer.alert('请上传二维码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var customerServiceExplain = that.customerServiceExplain;
			if (customerServiceExplain === null || customerServiceExplain === '') {
				layer.alert('请输入客服说明', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var customerServiceUrl = that.customerServiceUrl;
			if (customerServiceUrl === null || customerServiceUrl === '') {
				layer.alert('请输入图片点击链接', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/masterControl/updateCustomerQrcodeSetting', {
				qrcodeStorageId : that.qrcodeStorageId,
				customerServiceUrl : that.customerServiceUrl,
				customerServiceExplain : that.customerServiceExplain
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadCustomerQrcodeSetting();
			});
		},

		refreshCache : function() {
			var cacheItems = [];
			var that = this;
			if (that.refreshConfigItem) {
				cacheItems.push('config*');
			}
			if (that.refreshDict) {
				cacheItems.push('dict*');
			}
			if (cacheItems.length == 0) {
				layer.alert('请勾选要刷新的缓存项', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/masterControl/refreshCache', cacheItems).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
			});
		}
	}
});