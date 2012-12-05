<%@ Page Title="Home Page" Language="vb" MasterPageFile="~/Site.Master" AutoEventWireup="false"
    CodeBehind="Exam.aspx.vb" Inherits="TestCreation._Default" %>

<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
    <script language="javascript" type="text/javascript">
// <![CDATA[


        function Radio1_onclick() {
            
        }

// ]]>
    </script>
<style type="text/css">
    .style1
    {
        width: 831px;
    }
</style>
</asp:Content>
<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
    <h2>
        </h2>
    <asp:Panel ID="Panel4" runat="server">
        Exam<br />
        <asp:ListBox ID="LBExam" runat="server" Width="899px">
            <asp:ListItem Selected="True">2012W1 Final</asp:ListItem>
            <asp:ListItem>2011W2 Final</asp:ListItem>
            <asp:ListItem>2010W1 Final</asp:ListItem>
            <asp:ListItem>2009W2 Final</asp:ListItem>
        </asp:ListBox>
    </asp:Panel>
<h2>
        2012W1 Exam 1</h2>
    <p>
        <table style="width: 100%; margin-bottom: 0px;">
            <tr>
                <td class="style1">
    <asp:Panel ID="Panel1" runat="server" BorderColor="Black" Width="628px">
        Question 1<br />
        <asp:DropDownList ID="DropDownList1" runat="server">
            <asp:ListItem Selected="True">Multiple Choice</asp:ListItem>
            <asp:ListItem>True/False</asp:ListItem>
            <asp:ListItem>Short Answer</asp:ListItem>
            <asp:ListItem>Fill in the Blank</asp:ListItem>
        </asp:DropDownList>
        &nbsp;<asp:CheckBox ID="CheckQ1" runat="server" 
            Text="Keep this Question for the later use?" AutoPostBack="True" />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Button ID="ShowButQ1" runat="server" Text="Hide" TabIndex="1" />
        <br />
        <asp:Panel ID="Panel2" runat="server">
            <asp:TextBox ID="TextBoxQ1" runat="server" Enabled="False" Height="44px" 
                Width="610px">1. Which of the follow is not a part of Gulf of Execution?</asp:TextBox>
            <br />
            &nbsp;<asp:RadioButtonList ID="RBListQ1" runat="server">
                <asp:ListItem>A. Execution</asp:ListItem>
                <asp:ListItem>B. Evaluation</asp:ListItem>
            </asp:RadioButtonList>
            <asp:Label ID="LQ11" runat="server" Text="A. " Visible="False"></asp:Label>
            <asp:TextBox ID="TextBoxQ11" runat="server" Enabled="False" Visible="False" 
                Width="400px">Execution</asp:TextBox>
            <br />
            <asp:Label ID="LQ12" runat="server" Text="B. " Visible="False"></asp:Label>
            <asp:TextBox ID="TextBoxQ12" runat="server" Enabled="False" Visible="False" 
                Width="400px">Evaluation</asp:TextBox>
            <br />
            <asp:Label ID="LQ13" runat="server" Text="C. " Visible="False"></asp:Label>
            <asp:TextBox ID="TextBoxQ13" runat="server" Enabled="False" Visible="False" 
                Width="400px"></asp:TextBox>
            <br />
            &nbsp;<asp:Button ID="AddButQ1" runat="server" Text="+" TabIndex="2" />
            <asp:Button ID="EditQ1" runat="server" style="width: 37px" Text="Edit" />
            <br />
            <br />
            Correct Answer:
            <asp:DropDownList ID="AnswerDDListQ1" runat="server" Visible="False">
            </asp:DropDownList>
            <asp:Label ID="LAnswerQ1" runat="server"></asp:Label>
        </asp:Panel>
    </asp:Panel>
&nbsp;</td>
                <td>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                    Questions&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <asp:ListBox ID="LBQuestion" runat="server" Height="250px" 
                        Width="211px"></asp:ListBox>
                </td>
            </tr>
        </table>
        </p>
<p>
        &nbsp;<asp:Panel ID="Panel3" runat="server" Width="628px">
    Question 2<br />
    <asp:DropDownList ID="DropDownList2" runat="server">
        <asp:ListItem>Multiple Choice</asp:ListItem>
        <asp:ListItem Selected="True">True/False</asp:ListItem>
        <asp:ListItem>Short Answer</asp:ListItem>
        <asp:ListItem>Fill in the Blank</asp:ListItem>
    </asp:DropDownList>
    <asp:CheckBox ID="CheckQ2" runat="server" 
        Text="Keep this Question for the later use?" AutoPostBack="True" />
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <asp:Button ID="ShowButQ2" runat="server" Text="Show" />
    <br />
    <asp:TextBox ID="TextBoxQ2" runat="server" Enabled="False" Height="34px" 
        Width="564px">2. Is it ethical for professors to ask whether or not student dislike any aspect of the course?</asp:TextBox>
    <br />
</asp:Panel>
<br />
<br />
<br />
</asp:Content>
