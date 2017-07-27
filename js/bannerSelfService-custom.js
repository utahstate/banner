var xe = xe || {};
xe.mep = $('meta[name=ssbMepDesc]').attr('content');
xe.configUrl = '/BannerExtensibility/theme';
xe.themeName = xe.mep || 'usu';
xe.autoReload = function( name ) {
return '&reload=' + new Date().getTime();
}
xe.loadTheme = function( configUrl, name ) {
var newLink = document.createElement( 'link' );
newLink.rel = 'stylesheet';
newLink.type = 'text/css';
newLink.href = configUrl + '/getTheme?name=' + name /*+ xe.autoReload()*/;
document.getElementsByTagName("head")[0].appendChild( newLink );
}
xe.loadTheme( xe.configUrl, xe.themeName );
