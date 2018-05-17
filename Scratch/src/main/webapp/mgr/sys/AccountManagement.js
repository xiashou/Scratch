/*
 * 系统账户管理
 * xs
 * 2018/04/25
 */
Ext.define('System.AccountManagement', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'account-mgr',

    init : function(){
        this.launcher = {
            text: '系统账户管理',
            iconCls:'account'
        };
    },
    
    storeId: -1,
    
    sStore: new Ext.data.Store({
		fields: ['id', 'name'],
		proxy : {
			type : 'ajax',
			url : '/biz/store/queryListByConfig.atc',
			reader : {
				root : 'storeList'
			}
		}
	}),
    
    store: new Ext.data.Store({
		fields: ['id', 'userName', 'password', 'roleId', 'locked', 'createdTime', 'lastLogin'],
		proxy : {
			type : 'ajax',
			url : '/sys/user/queryListByStore.atc',
			reader : {
				root : 'userList'
			}
		}
	}),
	
	f: new Ext.form.FormPanel({
		layout : 'anchor',
		defaults : {
			anchor : '100%',
			layout : 'hbox',
			xtype : 'fieldcontainer'
		},
		bodyPadding : '5 10 0 0',
		border : false,
		items : [ {
			defaults : {flex : 1,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				name : 'sysUser.userName',
				fieldLabel : '账号',
			},{
				name : 'sysUser.id',
				xtype: 'hiddenfield'
			}]
		},{
			defaults : {flex : 1,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
			items : [{
				name: 'sysUser.password',
				fieldLabel : '密码'
			},{
				name: 'sysUser.roleId',
				xtype: 'hiddenfield'
			}]
		},{
			defaults : {flex : 1,xtype : 'radiogroup',labelWidth : 70,labelAlign : 'right'},
			items: [{
				fieldLabel: '是否锁定', 
				defaults: {name: 'sysUser.locked'},
	        	items: [{inputValue: false, boxLabel: '启用', checked: true}, {inputValue: true, boxLabel: '锁定'}]
			}]
		} ]
	}),
	
	buttons: function(me) {
		var desktop = me.app.getDesktop();
		return [{
			text : '保 存',
			iconCls : 'accept',
			handler : function() {
				if (me.f.form.isValid()) {
					me.f.form.submit({
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							subwin_accountmgr.hide();
							desktop.showMessage(action.result.msg);
							me.store.reload();
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.MessageBox.alert('提示', msg);
						}
					});
				}
			}
		}, {
			text : '关 闭 ',
			iconCls : 'stop',
			handler : function() {
				subwin_accountmgr.hide();
			}
		}];
	},
	
	selModel: Ext.create('Ext.selection.CheckboxModel', {
		injectCheckbox : 1,
		mode : 'SINGLE'
	}),
	
	tbar: function(me) {
		var desktop = me.app.getDesktop();
		return [{
            text:'新增',
            iconCls:'add',
            handler : function() {
            	subwin_accountmgr.setTitle('新建用户');
            	me.f.getForm().reset();
            	me.f.getForm().findField('sysUser.roleId').setValue(me.storeId);
            	me.f.getForm().findField('sysUser.userName').setReadOnly(false);
            	me.f.getForm().url = '/sys/user/insertSysUser.atc';
            	subwin_accountmgr.show();
			}
        }, {
            text:'修改',
            iconCls:'pencil',
            handler : function() {
            	var record = me.selModel.getSelection()[0];
        		if (Ext.isEmpty(record)) {
        			Ext.MessageBox.show({
        				title : '提示',
        				msg : '你没有选中任何项目！',
        				buttons : Ext.MessageBox.OK,
        				icon : Ext.MessageBox.INFO
        			});
        			return;
        		}
        		subwin_accountmgr.setTitle('修改用户');
            	me.f.getForm().reset();
            	me.f.getForm().url = '/sys/user/updateSysUser.atc';
            	me.selModel.deselectAll();
            	me.f.getForm().setValues([
            		{id:'sysUser.id', value:record.data.id},
					{id:'sysUser.userName', value:record.data.userName},
					{id:'sysUser.password', value:record.data.password},
					{id:'sysUser.roleId', value:me.storeId},
					{id:'sysUser.locked', value:record.data.locked}
				]);
            	me.f.getForm().findField('sysUser.userName').setReadOnly(true);
//            	me.f.getForm().findField('sysUser.locked').setValue({'sysUser.locked' : record.data.locked});
        		subwin_accountmgr.show();
			}
        }, {
            text:'删除',
            iconCls:'delete',
            handler: function(){
            	var record = me.selModel.getSelection()[0];
        		if (Ext.isEmpty(record)) {
        			Ext.MessageBox.show({
        				title : '提示',
        				msg : '你没有选中任何项目！',
        				buttons : Ext.MessageBox.OK,
        				icon : Ext.MessageBox.INFO
        			});
        			return;
        		}
        		Ext.Msg.confirm('请确认', '确定要删除这项吗?', function(btn, text) {
        			if (btn == 'yes') {
        				Ext.Ajax.request({
        					url : '/sys/user/deleteSysUser.atc',
        					params : {
        						'sysUser.id' : record.data.id
        					},
        					success : function(resp, opts) {
        						var result = Ext.decode(resp.responseText);
        						if (result.success) {
        							desktop.showMessage(result.msg);
        							me.selModel.deselectAll();
        							me.store.reload();
        						} else
        							Ext.MessageBox.show({
        								title : '提示',
        								msg : result.msg,
        								buttons : Ext.MessageBox.OK,
        								icon : Ext.MessageBox.ERROR
        							});
        					},
        					failure : function(resp, opts) {
        						var result = Ext.decode(resp.responseText);
        						Ext.MessageBox.show({
        							title : '提示',
        							msg : result.msg,
        							buttons : Ext.MessageBox.OK,
        							icon : Ext.MessageBox.ERROR
        						});
        					}
        				});
        			}
        		});
            }
        }];
	},

    createWindow : function(){
    	var me = this;
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('account-mgr');
        
        if(!win){
            win = desktop.createWindow({
                id: 'account-mgr',
                title:'系统账户管理',
                width:900,
                height:580,
                iconCls: 'account',
                animCollapse:false,
                constrainHeader:true,
                layout : 'border',
        		items : [{
        			title : '商户',
        			tools : [{
        				type:'refresh',
        				handler : function() {
        					me.sStore.reload();
        				}
        			}],
        			collapsible : true,
        			width : '20%',
        			split : true,
        			region : 'west',
        			layout : 'fit',
        			border : false,
        			items : [{
        				border: false,
                        xtype: 'grid',
                        store: me.sStore,
                        stripeRows : true,
                        frame : false,
                        forceFit: true,
                        viewConfig : {enableTextSelection : true},
                		loadMask : {msg : '正在加载表格数据,请稍等...'},
                		columns: [new Ext.grid.RowNumberer({
	                    	header : 'No',
	                		width : '15%'
	                    }), {
	                		dataIndex : 'id',
	                		hidden: true
	                    },{
	                    	text : '商户',
	                    	dataIndex : 'name',
	                		width : '85%'
	                    }],
	                    listeners:{
	        		        itemclick: function(grid, record, item, index, e) {
	        		        	me.storeId = record.data.id;
	        		        	me.store.load({
	        						params: {
	        							roleId: me.storeId
	        						}
	        					});
	        		        }
	        		    }
        			}]
        		}, {
        			title : '用户',
        			region : 'center',
        			split : true,
        			layout : 'fit',
        			border : false,
        			items : [{
        				border: false,
                        xtype: 'grid',
                        store: me.store,
                        selModel: me.selModel,
                        stripeRows : true,
                        frame : false,
                        forceFit: true,
                        viewConfig : {enableTextSelection : true},
                		loadMask : {msg : '正在加载表格数据,请稍等...'},
                		columns: [new Ext.grid.RowNumberer({
	                    	header : 'No',
	                		width : '5%'
	                    }), {
	                		dataIndex : 'id',
	                		hidden: true
	                    },{
	                    	text : '账号',
	                    	dataIndex : 'userName',
	                		width : '15%'
	                    },{
	                    	text : '加密密码',
	                    	dataIndex : 'password',
	                    	width : '23%'
	                    },{
	                    	text : '是否锁定',
	                		dataIndex : 'locked',
	                		width : '12%',
	                		renderer: function(value){
	            				return value ? '<font color=red>锁定</font>':'<font color=green>启用</font>'
	            			}
	                    },{
	                    	text : '最后登录时间',
	                    	dataIndex : 'lastLogin',
	                    	width : '25%'
	                    },{
	                    	text : '创建时间',
	                    	dataIndex : 'createdTime',
	                    	width : '25%'
	                    }],
	                    tbar: me.tbar(me)
        			}]
        		}, subwin_accountmgr = Ext.create('Ext.Window', {
                    width: 350,
                    height: 180,
                    constrain: true,
                    layout: 'fit',
                    closeAction: 'hide',
                    items: [me.f],
                    buttons: me.buttons(me)
                })],
                listeners: {
                    show: function() {
                    	me.sStore.load();
                    }
                }
            });
        }
        return win;
    }

});

