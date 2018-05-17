/*
 * Banner图片管理
 * xs
 * 2018/04/03
 */
Ext.define('Business.BannerManagement', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'banner-mgr',

    init : function(){
        this.launcher = {
            text: 'Banner图片管理',
            iconCls:'banner'
        };
    },
    
    store: new Ext.data.Store({
		pageSize : 20,
		fields: ['id', 'appid', 'bannerUrl', 'linkUrl', 'sortNo'],
		proxy : {
			type : 'ajax',
			url : '/biz/banner/queryBannerList.atc',
			reader : {
				root : 'bannerList'
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
			defaults : {
				flex : 1,
				xtype : 'textfield',
				labelWidth : 70,
				labelAlign : 'right'
			},
			items : [ {
				xtype : 'numberfield',
				name : 'banner.sortNo',
				fieldLabel : '排序',
			},{
				name : 'banner.id',
				xtype: 'hiddenfield'
			}]
		},{
			defaults : {
				flex : 1,
				xtype : 'filefield',
				labelWidth : 70,
				labelAlign : 'right'
			},
			items : [ {
				fieldLabel : '图片',
				name: 'upload',
				buttonText: '浏览...',
				anchor : '99%'
			} ]
		},{
			defaults : {
				flex : 1,
				xtype : 'textfield',
				labelWidth : 70,
				labelAlign : 'right'
			},
			items : [ {
				fieldLabel : '链接地址',
				name: 'banner.linkUrl',
				anchor : '99%'
			} ]
		},{
			defaults : {
				flex : 1,
				xtype : 'displayfield',
				labelWidth : 70
			},
			items: [ {
				fieldLabel : '&nbsp;',
				labelSeparator: '',
				value:'图片最佳尺寸864*470'
			} ]
		} ]
	}),
	
	buttons: function(me){
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
							subwin_bannermgr.hide();
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
				subwin_bannermgr.hide();
			}
		}];
	},
	
	picRenderer: function(value, metaData, record) {
		if(value){
  			metaData.tdAttr = "data-qtip=\"<img src='/upload/banner/" + record.data.appid + '/' + value + "' style='width:320px; height:150px'/>\""; 
  			return '<img src="/upload/banner/' + record.data.appid + '/' + value + '" style="height:30px" onerror="this.src=\'/resources/img/noImage.png\'" />';
  		} else
  			return '<img src="/resources/img/noImage.png" style="height:30px" />';
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
            	subwin_bannermgr.setTitle('新建Banner');
            	me.f.getForm().reset();
            	me.f.getForm().url = '/biz/banner/insertBanner.atc';
            	subwin_bannermgr.show();
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
        					url : '/biz/banner/deleteBanner.atc',
        					params : {
        						'banner.id' : record.data.id
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
        }]
	},

    createWindow : function(){
    	var me = this;
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('banner-mgr');
    	
        if(!win){
            win = desktop.createWindow({
                id: 'banner-mgr',
                title:'Banner图片管理',
                width:850,
                height:540,
                iconCls: 'banner',
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                items: [{
                		border: false,
                        xtype: 'grid',
                        store: me.store,
                        selModel: me.selModel,
                        stripeRows : true,
                        frame : false,
                        viewConfig : {enableTextSelection : true},
                		loadMask : {msg : '正在加载表格数据,请稍等...'},
                        columns: [new Ext.grid.RowNumberer({
	                        	header : 'No',
	                    		width : '4%'
	                        }), {
	                    		dataIndex : 'id',
	                    		hidden: true
	                        },{
	                        	text : '图片',
	                        	dataIndex : 'bannerUrl',
	                    		width : '27%',
	                    		renderer: me.picRenderer
	                        },{
	                        	text : '链接地址',
	                    		dataIndex : 'linkUrl',
	                    		width : '55%'
	                        },{
	                        	text : '排序',
	                        	dataIndex : 'sortNo',
	                        	width : '10%'
	                        }]
	                    }, subwin_bannermgr = Ext.create('Ext.Window', {
	                        width: 400,
	                        height: 220,
	                        constrain: true,
	                        layout: 'fit',
	                        closeAction: 'hide',
	                        items: [me.f],
	                        buttons: me.buttons(me)
	                    })
                ],
                tbar:me.tbar(me),
                listeners: {
                    show: function() {
                    	me.store.load();
                    }
                }
            });
        }
        
        return win;
    }
    
    

});

