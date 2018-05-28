/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('MyDesktop.App', {
    extend: 'Ext.ux.desktop.App',

    requires: [
        'Ext.window.MessageBox',

        'Ext.ux.desktop.ShortcutModel',

        'MyDesktop.SystemStatus',
        'MyDesktop.VideoWindow',
        'MyDesktop.GridWindow',
        'MyDesktop.TabWindow',
        'MyDesktop.AccordionWindow',
        'MyDesktop.Notepad',
        'MyDesktop.BogusMenuModule',
        'MyDesktop.BogusModule',
//        'MyDesktop.DeptReport',
//        'MyDesktop.Blockalanche',
        
        'System.AccountManagement',			//系统用户管理
        
        'Business.StoreManagement',			//门店管理
        'Business.CouponManagement',		//商家优惠券管理
        'Business.ActCouponManagement',		//活动优惠券管理
        'Business.ScratchSetting',			//刮奖设置
        'Business.MemberReport',			//会员报表
        'Business.BannerManagement',		//Banner管理
        'Business.ActivityManagement',		//活动管理
        'Business.MemCouponManagement',		//会员优惠券管理
        'Business.MemScratchManagement',	//会员刮奖管理
//        'MyDesktop.WinningManagement',	//中奖报表
        
        'MyDesktop.Settings'
    ],

    init: function() {
        // custom logic before getXYZ methods get called...

        this.callParent();

        // now ready...
    },

    getModules : function(){
    	if(BB == 0){
    		return [
    			new System.AccountManagement(),
    			new Business.BannerManagement(),
    			new Business.StoreManagement(),
    			new Business.ActivityManagement(),
    			new Business.ActCouponManagement(),
    			new Business.MemCouponManagement(),
    			new Business.MemScratchManagement(),
    			new Business.ScratchSetting(),
    			new Business.MemberReport()
          ];
    	} else {
    		return [
    			new Business.CouponManagement()
    		];
    	}
    },

    getDesktopConfig: function () {
        var me = this, ret = me.callParent(), store;
        
        if(BB == 0){
        	store = Ext.create('Ext.data.Store', {
                model: 'Ext.ux.desktop.ShortcutModel',
                data: [
                    { name: 'Banner图片管理', iconCls: 'banner-shortcut', module: 'banner-mgr'},
                    { name: '商户管理', iconCls: 'store-shortcut', module: 'store-mgr'},
                    { name: '活动管理', iconCls: 'activity-shortcut', module: 'activity-mgr'},
                    { name: '会员报表', iconCls: 'member-shortcut', module: 'member-rpt'},
                    { name: '优惠券管理', iconCls: 'coupon-shortcut', module: 'actcoupon-mgr'},
                    { name: '会员优惠券管理', iconCls: 'memcou-shortcut', module: 'memcou-mgr'},
                    { name: '会员刮奖管理', iconCls: 'memscr-shortcut', module: 'memscr-mgr'},
                    { name: '刮奖设置', iconCls: 'setting-shortcut', module: 'scratch-set'},
                    { name: '系统账户管理', iconCls: 'account-shortcut', module: 'account-mgr'}
                ]
            });
        } else {
        	store = Ext.create('Ext.data.Store', {
                model: 'Ext.ux.desktop.ShortcutModel',
                data: [
                    { name: '优惠券管理', iconCls: 'coupon-shortcut', module: 'coupon-mgr'},
                ]
            });
        }

        return Ext.apply(ret, {
            //cls: 'ux-desktop-black',
            contextMenuItems: [
                { text: '更改设置', handler: me.onSettings, scope: me }
            ],
            shortcuts: store,
            wallpaper: '/resources/img/wallpapers/Blue-Sencha.jpg',
            wallpaperStretch: false
        });
    },

    // config for the start menu
    getStartConfig : function() {
        var me = this, ret = me.callParent();

        return Ext.apply(ret, {
            title: CC,
            iconCls: 'user',
            height: 400,
            toolConfig: {
                width: 100,
                items: [
                    {
                        text:'设置',
                        iconCls:'settings',
                        handler: me.onSettings,
                        scope: me
                    },
                    '-',
                    {
                        text:'注销',
                        iconCls:'logout',
                        handler: me.onLogout,
                        scope: me
                    }
                ]
            }
        });
    },

    getTaskbarConfig: function () {
        var ret = this.callParent();

        return Ext.apply(ret, {
            quickStart: [
                //{ name: 'Accordion Window', iconCls: 'accordion', module: 'acc-win' },
                //{ name: 'Grid Window', iconCls: 'icon-grid', module: 'grid-win' }
            ],
            trayItems: [
                { xtype: 'trayclock', flex: 1 }
            ]
        });
    },

    onLogout: function () {
        Ext.MessageBox.show({
			title : '提示',
			msg : '确定退出系统吗?',
			width : 250,
			buttons : Ext.MessageBox.YESNO,
			animEl : Ext.getBody(),
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					window.location.href = '/logout.atc';
				}
			}
		});
    },

    onSettings: function () {
        var dlg = new MyDesktop.Settings({
            desktop: this.desktop
        });
        dlg.show();
    }
    
});
