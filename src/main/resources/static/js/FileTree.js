
Id= "101.200.61.174"
// Id= "localhost"
const FileTreePath = "http://"+Id+":9999/file/fileTree"
const fileDetail="http://"+Id+":9999/detils.html";
// 对应的最外层就是全部文件的就是-1层,然后点开显示的就是第0层。对应的pid为1即pid为0的那一层里面第一个子树的子树
$(function () {
      $.getJSON(FileTreePath, null, function (json) {
          index(json)
       });
})
//

function index(json) {
    function addHtml(pid, object) {
        let html = "";
        let hideChild = pid > 1;
        html += '<ul class="' + hideChild + '">'
        console.log(object);
        if (object===undefined){
            return ""
        }
        if (object.type === "fi") {
            //文件
        } else {
            //目录
            let children = object;
            console.log(children instanceof Array)
            if (children instanceof Array) {
                children.forEach(function (obj) {

                    let hasChild = obj.type === "directory";          //判断是否有子树
                    let className = hasChild ? '' : 'treeNode-empty';               //无子树则为空子树样式
                    let treeRoot_cls = pid === 0 ? 'treeNode-cur' : '';             //设置子树类名
                    //获取子树在第几层级
                    let distance = pid * 20 + "px";
                    let url = obj.url;
                    if (typeof (url) == "undefined") {
                        url = "empty";
                    }

                    // html模板内容添加 data-type="" opj common
                    html += `
                  <li>
                    <div class="treeNode ${className} ${treeRoot_cls}" style="padding-left: ${distance}" data-child="${hasChild}">
                      <i class="icon icon-control icon-plus" data-open="false"></i>
                      <i class="icon icon-file"></i>
                      
                      <span class="title" data-url="${url}" >${obj.name}</span>'
                    </div>
                    ${addHtml(pid + 1, obj)}                          
                  </li>`;


                })
            } else {
                let hasChild = object.type === "directory";          //判断是否有子树
                let className = hasChild ? '' : 'treeNode-empty';               //无子树则为空子树样式
                let treeRoot_cls = pid === 0 ? 'treeNode-cur' : '';             //设置子树类名
                //获取子树在第几层级
                let distance = pid * 20 + "px";
                let url = object.url;
                if (typeof (url) == "undefined") {
                    url = "empty";
                }

                // html模板内容添加
                html += `
                  <li>
                    <div class="treeNode ${className} ${treeRoot_cls}" style="padding-left: ${distance}" data-child="${hasChild}">
                      <i class="icon icon-control icon-plus" data-open="false"></i>
                      <i class="icon icon-file"></i>
                      <span class="title" data-url="${url}">${object.name}</span>'
                    </div>
                    ${addHtml(pid + 1, object.data)}                          
                  </li>`;
            }
        }

        html += '</ul>';
        // html+='</li></ul>';

        return html;
    }

    function check() {
        let item = this
        let hasChild = $(item).attr("data-child");

        let openStatus = true;                                                              //状态
        if (hasChild) {

            //如果没有子代时treeNode下个节点ul设置为none样式

            let icon_control = $(this).children("i.icon-control");
            openStatus = icon_control.attr("data-open");
            if (openStatus === "true") {
                $(item).next("ul").hide()
                icon_control.attr("class", "icon icon-control icon-plus")
                icon_control.attr("data-open", "false");
                icon_control.css("background-position", "-30px 0")
                //默认状态true显示的是加号符号
            } else {
                $(item).next("ul").show()
                icon_control.attr("data-open", "true");
                icon_control.attr("class", "icon icon-control icon-minus")
                icon_control.css("background-position", "-30px -22px")
                //打开后状态已经是false了,这个时候显示减号
            }
        }

    }

    let data = json.data;
    let pid = 0;
    let left_div = $("#left_div");
    let right_div = $("#right_div");
    right_div.html(addHtml(pid, data[1]))
    left_div.html(addHtml(pid, data[0]));
    let fileItem = $(".treeNode-cur");
    // let root_icon = fileItem.children(".icon-plus");   //获取加号图标
    // root_icon.attr("class", "icon icon-control icon-minus")
    // root_icon.attr("data-open", "true")
    //把图标换成减图标
    $("div.treeNode").each(function () {
        $(this).on("click", check)
    })
    $("ul.true").hide()
    $("span").each(function () {

        let url = $(this).attr("data-url");
        if (url!=="empty"){
            console.log(url)
            $(this).parent().unbind("click",check)
            $(this).on("click",function (){
                //跳转显示
             //   alert("跳转"+url)
                window.open(fileDetail+"?"+url);

            })
        }

    })
}