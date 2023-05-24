//定义一些变量

var websocket = null;
var shakeList = ["","shake-hard","shake-slow","shake-little","shake-horizontal","shake-vertical","shake-rotate","shake-opacity","shake-crazy"];
var shakeChinese = ["","可劲儿摇","雪花飘","瑟瑟发抖","左右摇摆","上下跳动","跷跷板","飘忽不定","放弃治疗"];
var b = '</div></div>';
var host = location.host;
var wsHost = "ws://localhost:9999/ws";
var focus = false;
var mute = 2;
var shieldMap = new Map();
var timer;
var shakeNum = 0;
var msgSwitchTips = '点击可开启/关闭消息通知';
var emojiTips = '表情包功能';
var clearTips = '清屏！！！';
var sendTips = '点击发送消息(回车也可发送消息)';
var onerrorMsg = "与服务器连接发生错误，请刷新页面重新进入！";
var oncloseMsg = '已与服务器断开连接！';
var unSupportWsMsg = "当前浏览器不支持 WebSocket";
var emojiPath = 'dist/img/';
var emojiHead = '<img class="emoji_icon" src="'+emojiPath;
var textHead = '⇤';
var emojiFoot = '">';
var textFoot = '⇥';

