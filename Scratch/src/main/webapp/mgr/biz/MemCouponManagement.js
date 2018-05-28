/*
 * 会员优惠券管理
 * xs
 * 2018/05/18
 */
Ext.define('Business.MemCouponManagement', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'memcou-mgr',

    init : function(){
        this.launcher = {
            text: '会员优惠券',
            iconCls:'memcou'
        };
    },
    
    store: new Ext.data.Store({
		pageSize : 20,
		fields: ['id', 'openid', 'actcouponId', 'code', 'endDate', 'status', 'createdTime', 'member.nickName', 'coupon.name'],
		proxy : {
			type : 'ajax',
			url : '/biz/memCoupon/queryListPage.atc',
			reader : {
				root : 'memCouponList',
				totalProperty: 'totalCount'
			}
		}
	}),
	
	f : new Ext.form.FormPanel({
		id : 'f',
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
				id : 'name',
				name : 'store.name',
				fieldLabel : '商户名称',
			},{
				id : 'phone',
				name : 'store.phone',
				fieldLabel : '联系电话',
			},{
				id : 'id',
				name : 'store.id',
				xtype: 'hiddenfield'
			}]
		},{
			defaults : {flex : 1,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
			items : [{
				id : 'address',
				name: 'store.address',
				fieldLabel : '地址'
			}]
		},{
			defaults : {flex : 8,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
			items: [ {
				id : 'locationx',
				name: 'store.locationx',
				fieldLabel : '地址x坐标',
				readOnly:true, 
				fieldStyle:'background-color: #F0F0F0;'
			},{
				id : 'locationy',
				name: 'store.locationy',
				fieldLabel : '地址y坐标',
				readOnly:true, 
				fieldStyle:'background-color: #F0F0F0;'
			}]
		},{
			defaults : {flex : 1,xtype : 'textarea',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				fieldLabel : '商户简介',
				id : 'introduction',
				name: 'store.introduction',
				rows: 3
			} ]
		},{
			defaults : {flex : 3,xtype : 'filefield',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				fieldLabel : '小图',
				id : 'head',
				name: 'head',
				buttonText: '浏览...',
				anchor : '99%'
			},{
				xtype : 'displayfield',
				flex : 1,
				labelWidth : 5,
				fieldLabel : '&nbsp;',
				labelSeparator: '',
				value:'最佳尺寸180*180'
			} ]
		},{
			defaults : {flex : 3,xtype : 'filefield',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				fieldLabel : '大图',
				id : 'image',
				name: 'image',
				buttonText: '浏览...',
				anchor : '99%'
			},{
				xtype : 'displayfield',
				flex : 1,
				labelWidth : 5,
				fieldLabel : '&nbsp;',
				labelSeparator: '',
				value:'最佳尺寸750*408'
			} ]
		},{
			defaults : {flex : 1,xtype : 'radiogroup',labelWidth : 70,labelAlign : 'right'},
			items: [{
				id:'enable', 
				fieldLabel: '是否可用', 
				defaults: {name: 'store.enable'},
	        	items: [{inputValue: true, boxLabel: '正常', checked: true}, {inputValue: false,boxLabel: '锁定'}]
			},{
				xtype : 'displayfield',
				flex : 1,
				fieldLabel : '&nbsp;',
				labelSeparator: '',
				value:''
			}]
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
							subwin_couponmgr.hide();
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
				subwin_couponmgr.hide();
			}
		}];
	},
	
	statusRenderer : function(value, metaData, record) {
		if(value == 0)
  			return '<font color=red>未消费</font>';
  		else if(value == 1)
  			return '<font color=green>已消费</font>';
  		else
  			return '未知';
    },
    
    selModel: Ext.create('Ext.selection.CheckboxModel', {
		injectCheckbox : 1,
		mode : 'SINGLE'
	}),
	
	tbar: function(me) {
		var desktop = me.app.getDesktop();
		return [{
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
        		subwin_memcoumgr.setTitle('修改优惠券');
        		me.f.getForm().reset();
            	me.f.getForm().url = '/biz/coupon/updateCoupon.atc';
            	me.selModel.deselectAll();
//            	me.f.loadRecord(record);
            	me.f.getForm().setValues([
            		{id:'coupon.id', value:record.data.id},
					{id:'coupon.storeId', value:BB},
					{id:'coupon.name', value:record.data.name},
					{id:'coupon.describ', value:record.data.describ},
					{id:'coupon.number', value:record.data.number},
					{id:'coupon.endDate', value:record.data.endDate}
				]);
            	subwin_memcoumgr.show();
			}
        }]
	},
	
    createWindow : function(){
    	var me = this;
        var desktop = me.app.getDesktop();
        var win = desktop.getWindow('memcou-mgr');
        
        var pagesizeCombo = desktop.getPagesizeCombo();
        var number = parseInt(pagesizeCombo.getValue());
	  	pagesizeCombo.on("select", function(comboBox) {
	  		bbar.pageSize = parseInt(comboBox.getValue());
	  		number = parseInt(comboBox.getValue());
	  		me.store.pageSize = parseInt(comboBox.getValue());
	  		me.store.reload({
	  			params : {
	  				start : 0,
	  				limit : bbar.pageSize
	  			}
	  		});
	  	});
	  	
	  	var bbar = new Ext.PagingToolbar({
	  		pageSize : number,
	  		store : me.store,
	  		displayInfo : true,
	  		displayMsg : '显示{0}条到{1}条,共{2}条',
	  		emptyMsg : "没有符合条件的记录",
	  		items : [ '-', '&nbsp;&nbsp;', pagesizeCombo ]
	  	});
	  	
        if(!win){
            win = desktop.createWindow({
                id: 'memcou-mgr',
                title:'会员优惠券',
                width:900,
                height:580,
                iconCls: 'memcou',
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
                    forceFit: true,
                    viewConfig : {enableTextSelection : true},
            		loadMask : {msg : '正在加载表格数据,请稍等...'},
            		columns: [new Ext.grid.RowNumberer({
	                    	header : 'No',
	                		width : '4%'
	                    }), {
	                		dataIndex : 'id',
	                		hidden: true
	                    },{
	                    	dataIndex : 'openid',
	                    	hidden: true
	                    },{
	                    	text : '会员昵称',
	                    	dataIndex : 'member.nickName',
	                		width : '20%'
	                    },{
	                    	text : '优惠券',
	                    	dataIndex : 'coupon.name',
	                    	width : '15%'
	                    },{
	                    	text : '代码',
	                		dataIndex : 'code',
	                		width : '15%'
	                    },{
	                    	text : '到期日期',
	                    	dataIndex : 'endDate',
	                    	width : '15%'
	                    },{
	                    	text : '状态',
	                    	dataIndex : 'status',
	                    	width : '10%',
	                    	renderer: me.statusRenderer
	                    },{
	                    	text : '购买时间',
	                    	dataIndex : 'createdTime',
	                    	width : '15%'
	                    }]
                    }, subwin_memcoumgr = Ext.create('Ext.Window', {
                        width: 520,
                        height: 350,
                        constrain: true,
                        layout: 'fit',
                        closeAction: 'hide',
                        items: [me.f],
                        buttons: me.buttons(me)
                    })
                ],
//                tbar:me.tbar(me),
                bbar:bbar,
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

