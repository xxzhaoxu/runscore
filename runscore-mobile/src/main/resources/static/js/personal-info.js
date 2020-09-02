var personalInfoVM = new Vue({
	el : '#personal-info',
	data : {
		global : GLOBAL,
		accountInfo : '',
		showPersonalInfoFlag : true,

		modifyLoginPwdFlag : false,
		oldLoginPwd : '',
		newLoginPwd : '',
		confirmLoginPwd : '',

		modifyMoneyPwdFlag : false,
		oldMoneyPwd : '',
		newMoneyPwd : '',
		confirmMoneyPwd : '',

		homePageUrl : '',
		secretKeyFlag : false,
		secretKey : '',

		bindGoogleAuthFlag : false,
		userNameWithGoogleAuth : '',
		googleSecretKey : null,
		googleAuthBindTime : null,
		googleVerCode : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '个人信息';
		headerVM.showBackFlag = true;
		this.getUserAccountInfo();
		this.getHomePageUrl();
	},
	methods : {
		/**
		 * 获取用户账号信息
		 */
		getUserAccountInfo : function() {
			var that = this;
			that.$http.get('/userAccount/getUserAccountInfo').then(function(res) {
				if (res.body.data != null) {
					that.accountInfo = res.body.data;
				}
			});
		},

		getHomePageUrl : function() {
			var that = this;
			that.$http.get('/dictconfig/findConfigValueInCache', {
				params : {
					configItemCode : 'homePageUrl'
				}
			}).then(function(res) {
				this.homePageUrl = res.body.data;
			});
		},

		showSecretKeyPage : function() {
			headerVM.showBackFlag = false;
			headerVM.title = '账号密钥';
			this.showPersonalInfoFlag = false;
			this.secretKeyFlag = true;
			this.getSecretKey();
		},
		getSecretKey : function() {
			var that = this;
			that.$http.get('/userAccount/getUserAccountInfo').then(function(res) {
				that.secretKey = res.body.data.secretKey;
				if (that.secretKey == null || that.secretKey == '') {
					that.updateSecretKey();
				} else {
					that.$nextTick(() => {
						$('.code-content').html('');
						jQuery('.code-content').qrcode({
							width : 200,
							height : 200,
							text : that.homePageUrl + '/' + that.secretKey
						});
		            });
				}
			});
		},

		updateSecretKey : function() {
			var that = this;
			that.$http.get('/userAccount/updateSecretKey').then(function(res) {
				that.getSecretKey();
			});
		},

		showGoogleSecretKeyPage : function() {
			headerVM.showBackFlag = false;
			headerVM.title = '谷歌验证器';
			this.showPersonalInfoFlag = false;

			var that = this;
			that.$http.get('/userAccount/getGoogleAuthInfo', {
				params : {
				}
			}).then(function(res) {
				that.bindGoogleAuthFlag = true;
				that.userNameWithGoogleAuth = that.accountInfo.userName;
				that.googleVerCode = null;
				that.googleSecretKey = res.body.data.googleSecretKey;
				that.googleAuthBindTime = res.body.data.googleAuthBindTime;
				if (that.googleAuthBindTime == null) {
					layer.alert('首次绑定,系统自动分配谷歌密钥', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					that.generateGoogleSecretKey();
				} else {
					that.generateGoogleQRcode();
				}
			});
		},

		generateGoogleSecretKey : function() {
			var that = this;
			that.$http.get('/userAccount/generateGoogleSecretKey').then(function(res) {
				that.googleSecretKey = res.body.data;
				that.generateGoogleQRcode();
			});
		},

		generateGoogleQRcode : function() {
			var that = this;
			that.$nextTick(() => {
				$('.code-content').html('');
				jQuery('.code-content').qrcode({
					width : 200,
					height : 200,
					text : 'otpauth://totp/' + that.userNameWithGoogleAuth + '?secret=' + that.googleSecretKey
				});
            });
		},

		bindGoogleAuth : function() {
			var that = this;
			if (that.googleVerCode === null || that.googleVerCode === '') {
				layer.alert('请输入谷歌验证码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/bindGoogleAuth', {
				googleSecretKey : that.googleSecretKey,
				googleVerCode : that.googleVerCode
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('绑定成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.bindGoogleAuthFlag = false;
				that.refreshTable();
			});
		},

		hideSecretKeyPage : function() {
			headerVM.showBackFlag = true;
			headerVM.title = '个人信息';
			this.showPersonalInfoFlag = true;
			this.secretKeyFlag = false;
			this.bindGoogleAuthFlag = false;
		},


		hideModifyLoginPwdPage : function() {
			headerVM.showBackFlag = true;
			headerVM.title = '个人信息';
			this.showPersonalInfoFlag = true;
			this.modifyLoginPwdFlag = false;
		},

		showModifyLoginPwdPage : function() {
			headerVM.showBackFlag = false;
			headerVM.title = '修改登录密码';
			this.showPersonalInfoFlag = false;
			this.modifyLoginPwdFlag = true;
			this.oldLoginPwd = '';
			this.newLoginPwd = '';
			this.confirmLoginPwd = '';
		},

		/**
		 * 修改登录密码
		 */
		modifyLoginPwd : function() {
			var that = this;
			if (that.oldLoginPwd == null || that.oldLoginPwd == '') {
				layer.alert('请输入旧的登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newLoginPwd == null || that.newLoginPwd == '') {
				layer.alert('请输入新的登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.confirmLoginPwd == null || that.confirmLoginPwd == '') {
				layer.alert('请输入确认登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newLoginPwd != that.confirmLoginPwd) {
				layer.alert('密码不一致', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var passwordPatt = /^[A-Za-z][A-Za-z0-9]{5,14}$/;
			if (!passwordPatt.test(that.newLoginPwd)) {
				layer.alert('登录密码不合法!请输入以字母开头,长度为6-15个字母和数字的密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/modifyLoginPwd', {
				oldLoginPwd : that.oldLoginPwd,
				newLoginPwd : that.newLoginPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('登录密码修改成功!', {
					icon : 1,
					time : 2000,
					shade : false
				});
				that.hideModifyLoginPwdPage();
			});
		},

		hideModifyMoneyPwdPage : function() {
			headerVM.showBackFlag = true;
			headerVM.title = '个人信息';
			this.showPersonalInfoFlag = true;
			this.modifyMoneyPwdFlag = false;
		},

		showModifyMoneyPwdPage : function() {
			headerVM.showBackFlag = false;
			headerVM.title = '修改资金密码';
			this.showPersonalInfoFlag = false;
			this.modifyMoneyPwdFlag = true;
			this.oldMoneyPwd = '';
			this.newMoneyPwd = '';
			this.confirmMoneyPwd = '';
		},

		/**
		 * 修改资金密码
		 */
		modifyMoneyPwd : function() {
			var that = this;
			if (that.oldMoneyPwd == null || that.oldMoneyPwd == '') {
				layer.alert('请输入旧的资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newMoneyPwd == null || that.newMoneyPwd == '') {
				layer.alert('请输入新的资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.confirmMoneyPwd == null || that.confirmMoneyPwd == '') {
				layer.alert('请输入确认资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newMoneyPwd != that.confirmMoneyPwd) {
				layer.alert('密码不一致', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/modifyMoneyPwd', {
				oldMoneyPwd : that.oldMoneyPwd,
				newMoneyPwd : that.newMoneyPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('资金密码修改成功!', {
					icon : 1,
					time : 2000,
					shade : false
				});
				that.hideModifyMoneyPwdPage();
			});
		}

	}
});