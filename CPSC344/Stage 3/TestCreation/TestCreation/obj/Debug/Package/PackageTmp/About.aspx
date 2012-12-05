<%@ Page Title="About Us" Language="vb" MasterPageFile="~/Site.Master" AutoEventWireup="false"
    CodeBehind="About.aspx.vb" Inherits="TestCreation.About" %>

<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
    <style type="text/css">
    .style1
    {
        width: 128px;
    }
</style>
</asp:Content>
<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
    <p>
    Summary</p>
<p>
    <asp:DropDownList ID="VerDDL" runat="server">
        <asp:ListItem Selected="True">Version 1</asp:ListItem>
        <asp:ListItem>Version 2</asp:ListItem>
        <asp:ListItem>Version 3</asp:ListItem>
    </asp:DropDownList>
</p>
<asp:Panel ID="Panel2" runat="server">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <br />
    <table bgcolor="#E9FFB9" style="width:100%;">
        <tr>
            <td>
                Question
            </td>
            <td>
                Question Number</td>
            <td>
                Type</td>
            <td class="style1">
                Total Score</td>
            <td>
                Prev Avg (%)</td>
            <td>
                Use for current Version?</td>
        </tr>
        <tr>
            <td>
                1</td>
            <td>
                2012W1 Final Q1</td>
            <td>
                MC</td>
            <td class="style1">
                5</td>
            <td>
                80.2</td>
            <td>
                <asp:CheckBox ID="CheckBox1" runat="server" />
            </td>
        </tr>
        <tr>
            <td>
                2</td>
            <td>
                2012W1 Final Q2</td>
            <td>
                TF</td>
            <td class="style1">
                5</td>
            <td>
                77.3</td>
            <td>
                <asp:CheckBox ID="CheckBox2" runat="server" />
            </td>
        </tr>
    </table>
</asp:Panel>
<p>
    <asp:Button ID="Button1" runat="server" Text="Save this Version" />
    <asp:Button ID="Button2" runat="server" Text="Close " />
</p>
<p>
    &nbsp;</p>
</asp:Content>
