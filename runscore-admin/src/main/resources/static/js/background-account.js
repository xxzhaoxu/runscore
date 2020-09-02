var accountManageVM = new Vue({
	el : '#background-account',
	data : {
		accountTypeDictItems : [],
		accountRole : [],
		rebateDictItems : [],
		accountStateDictItems : [],
		userName : '',
		viterUserName : '',
		realName : '',

		userCardFlag : false,
		cardWithStorageId : '',
		cardIsStorageId : '',
		cardTheStorageId : '',

		addUserAccountFlag : false,
		accountEditFlag : false,
		selectedAccount : {},
		selectedAccountId : '',

		modifyLoginPwdFlag : false,
		newLoginPwd : '',

		bankCards : [],
		bankCardFlag : false,

		virtualWallets : [],
		virtualWalletFlag : false,

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
		this.loadAccountTypeItem();
		this.loadAccountRole();
		this.loadAccountStateItem();
		this.initTable();
	},
	methods : {

		loadAccountTypeItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'accountType'
				}
			}).then(function(res) {
				this.accountTypeDictItems = res.body.data;
			});
		},
		loadAccountRole : function() {
			var that = this;
			that.$http.get('/role/findRoleList', {
				params : {
					dictTypeCode : 'accountType'
				}
			}).then(function(res) {
				this.accountRole = res.body.data;
			});
		},

		loadAccountStateItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'accountState'
				}
			}).then(function(res) {
				this.accountStateDictItems = res.body.data;
			});
		},


		initTable : function() {
			var that = this;
			$('.account-manage-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/userAccount/findUserAccountDetailsInfoBackgroundAccountByPage',
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100 ],
				sortable : true,
				sortName : 'cashDeposit',
				sortOrder : 'desc',
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber,
						propertie : params.sortName,
						direction : params.sortOrder,
						userName : that.userName,
						inviterUserName : that.viterUserName,
						realName : that.realName
					};
					console.log(params);
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
					title : '用户名',
					cellStyle : {
						classes : 'user-name'
					}
				}, {
					field : 'realName',
					title : '真实姓名'
				}, {
					field : 'mobile',
					title : '手机号'
				}, {
					field : 'stateName',
					title : '账号状态'
				}, {
					title : '谷歌验证器',
					formatter : function(value, row, index, field) {
						if (row.googleAuthBindTime == null) {
							return '未绑定';
						}
						return '<p>验证器密钥:' + row.googleSecretKey + '</p>' + '<p>绑定时间:' + row.googleAuthBindTime + '</p>';
					}
				}, {
					field : 'inviterUserName',
					title : '邀请人'
				}, {
					field : 'registeredTime',
					title : '注册时间',
					sortable : true,
					order : 'asc'
				}, {
					field : 'latelyLoginTime',
					title : '最近登录时间',
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						var html = template('account-action', {
							accountInfo : row
						});
						return html;
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.account-manage-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		showBindGoogleAuthModal : function(id, userName) {
			var that = this;
			that.$http.get('/userAccount/getGoogleAuthInfo', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				that.bindGoogleAuthFlag = true;
				that.selectedAccountId = id;
				that.userNameWithGoogleAuth = userName;
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
				userAccountId : that.selectedAccountId,
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

		showBankCardModal : function(id) {
			var that = this;
			that.$http.get('/bankCard/findBankCardByUserAccountId', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				that.bankCards = res.body.data;
				that.bankCardFlag = true;
			});
		},

		showVirtualWalletModal : function(id) {
			var that = this;
			that.$http.get('/virtualWallet/findVirtualWalletByUserAccountId', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				that.virtualWallets = res.body.data;
				that.virtualWalletFlag = true;
			});
		},

		showAdjustInviteCodeQuotaModal : function(id) {
			var that = this;
			layer.prompt({
				title : '请输入邀请码配额',
				formType : 0
			}, function(value, index) {
				if (value == null || value == '') {
					layer.alert('请输入邀请码配额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				layer.close(index);
				that.adjustInviteCodeQuota(id, value);
			});
		},

		adjustInviteCodeQuota : function(id, inviteCodeQuota) {
			var that = this;
			that.$http.post('/userAccount/adjustInviteCodeQuota', {
				userAccountId : id,
				inviteCodeQuota : inviteCodeQuota
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.refreshTable();
			});
		},


		openAddAccountModal : function() {
			this.addUserAccountFlag = true;
			this.selectedAccount = {
				inviterUserName : header.userName,
				userName : '',
				viterUserName : '',
				realName : '',
				mobile : '',
				loginPwd : '',
				accountType : '',
				roleId : '',
				state : ''
			}
		},

		addUserAccount : function() {
			var that = this;
			var selectedAccount = that.selectedAccount;
			if (selectedAccount.userName === null || selectedAccount.userName === '') {
				layer.alert('请输入用户名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.realName === null || selectedAccount.realName === '') {
				layer.alert('请输入真实姓名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.mobile === null || selectedAccount.mobile === '') {
				layer.alert('请输入手机号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.loginPwd === null || selectedAccount.loginPwd === '') {
				layer.alert('请输入登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.accountType === null || selectedAccount.accountType === '') {
				layer.alert('请选择账号类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.state === null || selectedAccount.state === '') {
				layer.alert('请选择状态', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/addUserAccount', selectedAccount, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addUserAccountFlag = false;
				that.refreshTable();
			});
		},

		delAccount : function(id) {
			var that = this;
			layer.confirm('确定要删除该账号吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/userAccount/delUserAccount', {
					params : {
						userAccountId : id
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
		},

		showAccountEditModal : function(id) {
			var that = this;
			that.$http.get('/userAccount/findUserAccountDetailsInfoById', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				that.selectedAccount = res.body.data;
				that.accountEditFlag = true;
			});
		},

		updateUserAccount : function() {
			var that = this;
			var selectedAccount = that.selectedAccount
			if (selectedAccount.userName === null || selectedAccount.userName === '') {
				layer.alert('请输入用户名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.realName === null || selectedAccount.realName === '') {
				layer.alert('请输入真实姓名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.mobile === null || selectedAccount.mobile === '') {
				layer.alert('请输入手机号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.accountType === null || selectedAccount.accountType === '') {
				layer.alert('请选择账号类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.state === null || selectedAccount.state === '') {
				layer.alert('请选择状态', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/updateUserAccount', selectedAccount, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.accountEditFlag = false;
				that.refreshTable();
			});
		},

		showModifyLoginPwdModal : function(id) {
			var that = this;
			that.$http.get('/userAccount/findUserAccountDetailsInfoById', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				that.selectedAccount = res.body.data;
				that.newLoginPwd = '';
				that.modifyLoginPwdFlag = true;
			});
		},

		modifyLoginPwd : function() {
			var that = this;
			if (that.newLoginPwd == null || that.newLoginPwd == '') {
				layer.alert('请输入登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/modifyLoginPwd', {
				userAccountId : that.selectedAccount.id,
				newLoginPwd : that.newLoginPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.modifyLoginPwdFlag = false;
				that.refreshTable();
			});
		},

		showBindBankInfoModal : function(row) {
			var that = this;
			that.$http.get('/userAccount/getBankInfo', {
				params : {
					userAccountId : row.id
				}
			}).then(function(res) {
				that.selectedAccount = row;
				that.bankInfo = res.body.data;
				that.bindBankInfoFlag = true;
			});
		},

		bindBankInfo : function() {
			var that = this;
			var bankInfo = that.bankInfo;
			if (bankInfo.openAccountBank == null || that.openAccountBank == '') {
				layer.alert('请输入开户银行', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (bankInfo.accountHolder == null || that.accountHolder == '') {
				layer.alert('请输入开户人姓名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (bankInfo.bankCardAccount == null || that.bankCardAccount == '') {
				layer.alert('请输入银行卡账号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/bindBankInfo', {
				userAccountId : that.selectedAccount.id,
				openAccountBank : bankInfo.openAccountBank,
				accountHolder : bankInfo.accountHolder,
				bankCardAccount : bankInfo.bankCardAccount
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.bindBankInfoFlag = false;
				that.refreshTable();
			});
		},
		 getImgUrl(row){
	        return "/storage/fetch/"+row;
	    },
		showUserCardModal : function(id) {
			var that = this;
			that.$http.get('/userAccount/findUserAccountDetailsInfoById', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				this.cardWithStorageId = res.body.data.cardWithStorageId;
				this.cardIsStorageId = res.body.data.cardIsStorageId;
				this.cardTheStorageId = res.body.data.cardTheStorageId;
				this.userCardFlag = true;
			});
		}

	}
});