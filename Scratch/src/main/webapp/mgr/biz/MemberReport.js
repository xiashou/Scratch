/*
 * 会员报表
 * xs
 * 2018/04/03
 */
Ext.define('Business.MemberReport', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'member-rpt',
    
    title: '会员报表',

    init : function(){
        this.launcher = {
            text: '会员报表',
            iconCls:'member'
        };
    },
    
    createNewWindow: function () {
        var me = this,
            desktop = me.app.getDesktop();

        me.store = Ext.create('Ext.data.Store',{
        	pageSize : 20,
    		fields: ['id', 'appid', 'unionId', 'openId', 'nickName', 'gender', 'language', 'city', 'province', 'country', 'avatarUrl', 'timestamp'],
    		proxy : {
    			type : 'ajax',
    			url : '/biz/member/queryMemberList.atc',
    			reader : {
    				root : 'memberList',
    				totalProperty: 'totalCount'
    			}
    		}
    	});
        
        var pagesizeCombo = desktop.getPagesizeCombo();
        var number = parseInt(pagesizeCombo.getValue());
        
	  	pagesizeCombo.on("select", function(comboBox) {
	  		me.bbar.pageSize = parseInt(comboBox.getValue());
	  		number = parseInt(comboBox.getValue());
	  		me.store.pageSize = parseInt(comboBox.getValue());
	  		me.store.reload({
	  			params : {
	  				start : 0,
	  				limit : me.bbar.pageSize
	  			}
	  		});
	  	});
	  	
	  	me.bbar = Ext.create('Ext.PagingToolbar',{
	  		pageSize : number,
	  		store : me.store,
	  		displayInfo : true,
	  		displayMsg : '显示{0}条到{1}条,共{2}条',
	  		emptyMsg : "没有符合条件的记录",
	  		items : [ '-', '&nbsp;&nbsp;', pagesizeCombo ]
	  	});
        
        me.f = Ext.create('Ext.form.FormPanel',{
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
    				name : 'activity.name',
    				fieldLabel : '产品标题',
    			},{
    				name : 'activity.id',
    				xtype: 'hiddenfield'
    			}]
    		},{
    			defaults : {flex : 1,xtype : 'numberfield',labelWidth : 70,labelAlign : 'right'},
    			items : [{
    				name : 'activity.price',
    				fieldLabel : '价格'
    			},{
    				name: 'activity.virNumber',
    				fieldLabel : '虚拟参与数'
    			}]
    		},{
    			defaults : {flex : 1,xtype : 'numberfield',labelWidth : 70,labelAlign : 'right'},
    			items : [{
    				name : 'activity.broNumber',
    				fieldLabel : '浏览人数',
    				readOnly: true,
    				fieldStyle:'background-color: #F0F0F0;'
    			},{
    				name: 'activity.actNumber',
    				fieldLabel : '实际参与数',
    				readOnly: true,
    				fieldStyle:'background-color: #F0F0F0;'
    			}]
    		},{
    			defaults : {flex : 5,xtype : 'filefield',labelWidth : 70,labelAlign : 'right'},
    			items : [ {
    				fieldLabel : '大图',
    				name: 'image',
    				buttonText: '浏览...',
    				anchor : '99%'
    			},{
    				xtype : 'displayfield',
    				flex : 2,
    				labelWidth : 5,
    				fieldLabel : '&nbsp;',
    				labelSeparator: '',
    				value:'最佳尺寸540*518'
    			} ]
    		},{
    			defaults : {flex : 1,xtype : 'radiogroup',labelWidth : 70,labelAlign : 'right'},
    			items: [{
    				fieldLabel: '是否开启', 
    				defaults: {name: 'activity.enable'},
    	        	items: [{inputValue: true, boxLabel: '开启'}, {inputValue: false,boxLabel: '停止', checked: true}]
    			},{
    				xtype : 'displayfield',
    				fieldLabel : '&nbsp;',
    				labelSeparator: '',
    				value:''
    			}]
    		} ]
    	});
        
        me.tbar = [
            {text:'查询', iconCls:'preview', handler: me.onSearch, scope: me }
        ];
        
        me.buttons = [
            {text:'查 询', iconCls:'preview', handler: me.onSubmit, scope: me },
            {text:'关 闭', iconCls:'stop', handler: me.onCancel, scope: me }
        ];
        
        me.headRenderer = function(value, metaData, record) {
    		if(value){
      			metaData.tdAttr = "data-qtip=\"<img src='" + value + "' style='width:120px; height:120px'/>\""; 
      			return '<img src="' + value + '" style="height:20px" onerror="this.src=\'/resources/img/noImage.png\'" />';
      		} else
      			return '<img src="/resources/img/noImage.png" style="height:20px" />';
        };
        
        me.subwin = Ext.create('Ext.Window', {
            width: 420,
            height: 230,
            constrain: true,
            layout: 'fit',
            closeAction: 'hide',
            items: [me.f],
            buttons: me.buttons
        });
    	
        return desktop.createWindow({
            id: me.id,
            title: me.title,
            width:900,
            height:580,
            iconCls: 'member',
            animCollapse:false,
            constrainHeader:true,
            layout: 'fit',
            items: [{
            	border: false,
                xtype: 'grid',
                store: me.store,
                stripeRows: true,
                frame: false,
                forceFit: true,
                viewConfig: {enableTextSelection : true},
        		loadMask: {msg : '正在加载表格数据,请稍等...'},
        		columns: [new Ext.grid.RowNumberer({
                    	header : 'No',
                		width : '4%'
                    }), {
                		dataIndex : 'id',
                		hidden: true
                    },{
                    	text : '头像',
                    	dataIndex : 'avatarUrl',
                		width : '6%',
                		renderer: me.headRenderer
                    },{
                    	text : '昵称',
                    	dataIndex : 'nickName',
                    	width : '25%'
                    },{
                    	text : '性别',
                		dataIndex : 'gender',
                		width : '10%',
                		renderer: function(value){
            				return value==1?'男':(value==2?'女':'未知');
            			}
                    },{
                    	text : '语言',
                    	dataIndex : 'language',
                    	width : '10%'
                    },{
                    	text : '城市',
                    	dataIndex : 'city',
                    	width : '10%'
                    },{
                    	text : '省份',
                    	dataIndex : 'province',
                    	width : '10%'
                    },{
                    	text : 'openId',
                    	dataIndex : 'openId',
                    	width : '21%'
                    }]
                }, me.subwin
            ],
            tbar: me.tbar,
            bbar: me.bbar,
            listeners: {
                show: function() {
                	me.store.load();
                }
            }
        });
    },

    createWindow : function(){
    	var win = this.app.getDesktop().getWindow(this.id);
        if (!win) {
            win = this.createNewWindow();
        }
        return win;
    },
    
    onSearch: function() {
    	var me = this;
    	me.subwin.setTitle('查询');
    	me.f.getForm().reset();
    	me.f.getForm().url = '/biz/activity/insertActivity.atc';
    	me.subwin.show();
    },
    
    onSubmit: function() {
    	var me = this, desktop = me.app.getDesktop();
    	if (me.f.form.isValid()) {
			me.f.form.submit({
				waitTitle : '提示',
				method : 'POST',
				waitMsg : '正在处理数据,请稍候...',
				success : function(form, action) {
					me.subwin.hide();
					desktop.showMessage(action.result.msg);
					me.store.reload();
				},
				failure : function(form, action) {
					var msg = action.result.msg;
					Ext.MessageBox.alert('提示', msg);
				}
			});
		}
    },
    
    onCancel: function() {
    	var me = this;
    	me.subwin.hide();
    }

});

