function RightMenu()
{
    this.AddExtendMenu = AddExtendMenu;
    this.AddItem = AddItem;
    this.GetMenu = GetMenu;
    this.HideAll = HideAll;
    this.I_OnMouseOver = I_OnMouseOver;
    this.I_OnMouseOut = I_OnMouseOut;
    this.I_OnMouseUp = I_OnMouseUp;
    this.P_OnMouseOver = P_OnMouseOver;
    this.P_OnMouseOut = P_OnMouseOut;
    A_rbpm = new Array();
    HTMLstr = "";
    HTMLstr += "<!-- RightButton PopMenu -->\n";
    HTMLstr += "\n";
    HTMLstr += "<!-- PopMenu Starts -->\n";
    HTMLstr += "<div id='E_rbpm' class='rm_div'>\n";
    HTMLstr += "<table width='100%' border='0' cellspacing='0' class='rmenu'>\n";
    HTMLstr += "<!-- Insert A Extend Menu or Item On Here For E_rbpm -->\n";
    HTMLstr += "</table>\n";
    HTMLstr += "</div>\n";
    HTMLstr += "<!-- Insert A Extend_Menu Area on Here For E_rbpm -->";
    HTMLstr += "\n";
    HTMLstr += "<!-- PopMenu Ends -->\n";
}
function AddExtendMenu(id, name, parent) {
    var TempStr = "";
    if (HTMLstr.indexOf("<!-- Extend Menu Area : E_" + id + " -->") != -1)
    {
        alert("E_" + id + "already exist!");
        return;
    }
    eval("A_" + parent + ".length++");
    eval("A_" + parent + "[A_" + parent + ".length-1] = id");
    TempStr += "<!-- Extend Menu Area : E_" + id + " -->\n";
    TempStr += "<div id='E_" + id + "' class='rm_div'>\n";
    TempStr += "<table width='100%' border='0' cellspacing='0' class='rmenu'>\n";
    TempStr += "<!-- Insert A Extend Menu or Item On Here For E_" + id + " -->";
    TempStr += "</table>\n";
    TempStr += "</div>\n";
    TempStr += "<!-- Insert A Extend_Menu Area on Here For E_" + id + " -->";
    TempStr += "<!-- Insert A Extend_Menu Area on Here For E_" + parent + " -->";
    HTMLstr = HTMLstr.replace("<!-- Insert A Extend_Menu Area on Here For E_" + parent + " -->", TempStr);


    eval("A_" + id + " = new Array()");
    TempStr = "";
    TempStr += "<!-- Extend Item : P_" + id + " -->\n";
    TempStr += "<tr id='P_" + id + "' class='out'";
    TempStr += " onmouseover='P_OnMouseOver(\"" + id + "\",\"" + parent + "\")'";
    TempStr += " onmouseout='P_OnMouseOut(\"" + id + "\",\"" + parent + "\")'";
    TempStr += " onmouseup=window.event.cancelBubble=true;";
    TempStr += " onclick=window.event.cancelBubble=true;";
    TempStr += "><td nowrap>";
    TempStr += "&nbsp;&nbsp;&nbsp;" + name + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td style='font-size:13px; text-align: right;'>4";
    TempStr += "</td></tr>\n";
    TempStr += "<!-- Insert A Extend Menu or Item On Here For E_" + parent + " -->";
    HTMLstr = HTMLstr.replace("<!-- Insert A Extend Menu or Item On Here For E_" + parent + " -->", TempStr);
}
function AddItem(id, name, parent, location)
{
    var TempStr = "";
    var ItemStr = "<!-- ITEM : I_" + id + " -->";
    if (id == "sperator")
    {
        TempStr += ItemStr + "\n";
        TempStr += "<tr style='height: 3;' class='out' onclick='window.event.cancelBubble=true;' onmouseup='window.event.cancelBubble=true;'><td colspan='2'><div class='sperator'></div></td></tr>";
        TempStr += "<!-- Insert A Extend Menu or Item On Here For E_" + parent + " -->";
        HTMLstr = HTMLstr.replace("<!-- Insert A Extend Menu or Item On Here For E_" + parent + " -->", TempStr);
        return;
    }
    if (HTMLstr.indexOf(ItemStr) != -1)
    {
        alert("I_" + id + "already exist!");
        return;
    }
    TempStr += ItemStr + "\n";
    TempStr += "<tr id='I_" + id + "' class='out'";
    TempStr += " onmouseover='I_OnMouseOver(\"" + id + "\",\"" + parent + "\")'";
    TempStr += " onmouseout='I_OnMouseOut(\"" + id + "\")'";
    TempStr += " onclick='window.event.cancelBubble=true;'";
    if (location == null)
        TempStr += " onmouseup='I_OnMouseUp(\"" + id + "\",\"" + parent + "\",null)'";
    else
        TempStr += " onmouseup='I_OnMouseUp(\"" + id + "\",\"" + parent + "\",\"" + location + "\")'";
    TempStr += "><td nowrap style='font-size:12px;'>";
    TempStr += "&nbsp;&nbsp;&nbsp;" + name +"&nbsp;&nbsp;&nbsp;";
    TempStr += "</td><td></td></tr>\n";
    TempStr += "<!-- Insert A Extend Menu or Item On Here For E_" + parent + " -->";
    HTMLstr = HTMLstr.replace("<!-- Insert A Extend Menu or Item On Here For E_" + parent + " -->", TempStr);
}
function GetMenu() {
    // alert(HTMLstr);
    return HTMLstr;
}
function I_OnMouseOver(id, parent)
{
    var Item;
    if (parent != "rbpm")
    {
        var ParentItem;
        ParentItem = eval("P_" + parent);
        ParentItem.className = "over";
    }
    Item = eval("I_" + id);
    Item.className = "over";
    HideAll(parent, 1);
}
function I_OnMouseOut(id)
{
    var Item;
    Item = eval("I_" + id);
    Item.className = "out";
}
function I_OnMouseUp(id, parent, location)
{
    var ParentMenu;
    window.event.cancelBubble = true;
    OnClick();
    ParentMenu = eval("E_" + parent);
    ParentMenu.display = "none";
    if (location == null)
        eval("Do_" + id + "()");
    else
        window.open(location);
}
function P_OnMouseOver(id, parent)
{
    var Item;
    var Extend;
    var Parent;
    if (parent != "rbpm")
    {
        var ParentItem;
        ParentItem = eval("P_" + parent);
        ParentItem.className = "over";
    }
    HideAll(parent, 1);
    Item = eval("P_" + id);
    Extend = eval("E_" + id);
    Parent = eval("E_" + parent);
    Item.className = "over";
    Extend.style.display = "block";
    Extend.style.posLeft = document.body.scrollLeft + Parent.offsetLeft + Parent.offsetWidth - 4;
    if (Extend.style.posLeft + Extend.offsetWidth > document.body.scrollLeft + document.body.clientWidth)
        Extend.style.posLeft = Extend.style.posLeft - Parent.offsetWidth - Extend.offsetWidth + 8;
    if (Extend.style.posLeft < 0) Extend.style.posLeft = document.body.scrollLeft + Parent.offsetLeft + Parent.offsetWidth;
    Extend.style.posTop = Parent.offsetTop + Item.offsetTop;
    if (Extend.style.posTop + Extend.offsetHeight > document.body.scrollTop + document.body.clientHeight)
        Extend.style.posTop = document.body.scrollTop + document.body.clientHeight - Extend.offsetHeight;
    if (Extend.style.posTop < 0) Extend.style.posTop = 0;
}
function P_OnMouseOut(id, parent)
{
}
function HideAll(id, flag)
{
    var Area;
    var Temp;
    var i;
    if (!flag)
    {
        Temp = eval("E_" + id);
        Temp.style.display = "none";
    }
    Area = eval("A_" + id);
    if (Area.length)
    {
        for (i = 0; i < Area.length; i++)
        {
            HideAll(Area[i], 0);
            Temp = eval("E_" + Area[i]);
            Temp.style.display = "none";
            Temp = eval("P_" + Area[i]);
            Temp.className = "out";
        }
    }
}

// document.onmouseup=OnMouseUp;
document.onclick = OnClick;

function OnMouseUp(event)
{
    if (event.button != 1)
    {
        var PopMenu;
        PopMenu = eval("E_rbpm");
        HideAll("rbpm", 0);
        PopMenu.style.display = "block";
        PopMenu.style.posLeft = document.body.scrollLeft + event.clientX;
        PopMenu.style.posTop = document.body.scrollTop + event.clientY;
        if (PopMenu.style.posLeft + PopMenu.offsetWidth > document.body.scrollLeft + document.body.clientWidth)
            PopMenu.style.posLeft = document.body.scrollLeft + document.body.clientWidth - PopMenu.offsetWidth;
        if (PopMenu.style.posLeft < 0) PopMenu.style.posLeft = 0;
        if (PopMenu.style.posTop + PopMenu.offsetHeight > document.body.scrollTop + document.body.clientHeight)
            PopMenu.style.posTop = document.body.scrollTop + document.body.clientHeight - PopMenu.offsetHeight;
        if (PopMenu.style.posTop < 0) PopMenu.style.posTop = 0;
    }
    //d.nid = "-1";
}
function OnClick() {
    HideAll("rbpm", 0);
}
// Add Your Function on following
function Do_viewcode() {
    window.location = "view-source:" + window.location.href;
}
function Do_help() {
    window.showHelp(window.location);
}
function Do_exit() {
    window.close();
}
function Do_refresh() {
    window.location.reload();
}
function Do_back() {
    history.back();
}
function Do_forward() {
    history.forward();
}
function Do_author() {
    alert("")
}
