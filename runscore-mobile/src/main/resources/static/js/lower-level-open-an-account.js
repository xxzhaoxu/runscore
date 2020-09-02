var lowerLevelOpenAnAccountVM = new Vue({
	el : '#lower-level-open-an-account',
	data : {
		global : GLOBAL,
		onlyOpenMemberAccount : true,
		onlyOpenAgentAccount : true,
		agentExplain : '',
		myChannels : [],
		accountType : '',
		lowerLevelChannels : [],
		inviteRegisterLink : null,
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '下级开户';
		headerVM.showBackFlag = true;
		this.loadRegisterSetting();
		this.loadMyReceiveOrderChannel();

		var clipboard = new ClipboardJS('#copyInviteRegisterLinkBtn');
		clipboard.on('success', function(e) {
			layer.alert('复制邀请链接成功!', {
				icon : 1,
				time : 3000,
				shade : false
			});
		});
	},
	methods : {

		loadRegisterSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getRegisterSetting').then(function(res) {
				that.onlyOpenMemberAccount = res.body.data.onlyOpenMemberAccount;
				that.onlyOpenAgentAccount = res.body.data.onlyOpenAgentAccount;
				that.agentExplain = res.body.data.agentExplain;
			});
		},

		loadMyReceiveOrderChannel : function() {
			var that = this;
			that.$http.get('/userAccount/findMyReceiveOrderChannel').then(function(res) {
				that.myChannels = res.body.data;
			});
		},

		addLowerLevelChannel : function() {
			this.lowerLevelChannels.push({
				channelId : '',
				rebate : ''
			});
		},

		generateRegiterLink : function() {
			var that = this;
			if (that.accountType === null || that.accountType === '') {
				layer.alert('请选择开户类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.lowerLevelChannels.length == 0) {
				layer.alert('至少增加一个通道', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var channelMap = new Map();
			for (var i = 0; i < that.lowerLevelChannels.length; i++) {
				var lowerLevelChannel = that.lowerLevelChannels[i];
				if (lowerLevelChannel.channelId === null || lowerLevelChannel.channelId === '') {
					layer.alert('请选择通道', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (lowerLevelChannel.rebate === null || lowerLevelChannel.rebate === '') {
					layer.alert('请设置返点', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				var key = lowerLevelChannel.channelId;
				if (channelMap.get(key) != null) {
					layer.alert('不能设置重复的通道', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				channelMap.set(key, key);
			}

			that.$http.post('/agent/generateInviteCodeAndGetInviteRegisterLink', {
				accountType : that.accountType,
				channelRebates : that.lowerLevelChannels
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.inviteRegisterLink = res.body.data.inviteCode.inviteRegisterLink;
				new QRCode(document.getElementById("qrcode"), that.inviteRegisterLink);  // 设置要生成二维码的链接
			});
		}
	}
});