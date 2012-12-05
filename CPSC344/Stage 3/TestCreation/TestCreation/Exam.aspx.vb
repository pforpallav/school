

Public Class _Default
    Inherits System.Web.UI.Page



    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load

        If AnswerDDListQ1.Items.Count <> RBListQ1.Items.Count Then
            AnswerDDListQ1.Items.Clear()
            For i = 0 To RBListQ1.Items.Count - 1
                AnswerDDListQ1.Items.Add(RBListQ1.Items.Item(i).Text)
            Next
        End If

        CheckQ1.Attributes.Add("CheckedChanged", "<script language=javascript1.2>window.location.reload(true);</script>")
        CheckQ2.Attributes.Add("CheckedChanged", "<script language=javascript1.2>window.location.reload(true);</script>")
    End Sub

    Protected Sub AddButQ1_Click(sender As Object, e As EventArgs) Handles AddButQ1.Click
        'RadioButtonQ13.Visible = True
        If RBListQ1.Items.Count < 3 Then
            TextBoxQ13.Visible = True
            TextBoxQ13.Enabled = True
            RBListQ1.Items.Add("C. ")
            LQ13.Visible = True
        End If

    End Sub

    Protected Sub TextBox2_TextChanged(sender As Object, e As EventArgs) Handles TextBoxQ11.TextChanged
        'RadioButtonQ11.Text = TextBoxQ11.Text
        RBListQ1.Items.Item(0).Text = LQ11.Text + TextBoxQ11.Text
        AnswerDDListQ1.Items.Item(0).Text = RBListQ1.Items.Item(0).Text

    End Sub



    Protected Sub TextBoxQ12_TextChanged(sender As Object, e As EventArgs) Handles TextBoxQ12.TextChanged
        RBListQ1.Items.Item(1).Text = LQ12.Text + TextBoxQ12.Text
        AnswerDDListQ1.Items.Item(1).Text = RBListQ1.Items.Item(1).Text
    End Sub

    Protected Sub TextBoxQ13_TextChanged(sender As Object, e As EventArgs) Handles TextBoxQ13.TextChanged
        'RadioButtonQ13.Text = TextBoxQ13.Text
        RBListQ1.Items.Item(2).Text = LQ13.Text + TextBoxQ13.Text
        AnswerDDListQ1.Items.Item(2).Text = RBListQ1.Items.Item(2).Text
    End Sub

    Protected Sub EditQ1_Click(sender As Object, e As EventArgs) Handles EditQ1.Click
        If EditQ1.Text = "Edit" Then
            TextBoxQ1.Enabled = True
            TextBoxQ11.Enabled = True
            TextBoxQ11.Visible = True
            TextBoxQ12.Enabled = True
            TextBoxQ12.Visible = True
            LQ11.Visible = True
            LQ12.Visible = True

            AnswerDDListQ1.Visible = True


            If RBListQ1.Items.Count = 3 Then
                TextBoxQ13.Enabled = True
                TextBoxQ13.Visible = True

                LQ13.Visible = True
            End If

            EditQ1.Text = "Done"
        ElseIf EditQ1.Text = "Done" Then
            TextBoxQ1.Enabled = False
            TextBoxQ11.Enabled = False
            TextBoxQ11.Visible = False
            TextBoxQ12.Enabled = False
            TextBoxQ12.Visible = False
            TextBoxQ13.Enabled = False
            TextBoxQ13.Visible = False
            LQ11.Visible = False
            LQ12.Visible = False
            LQ13.Visible = False

            EditQ1.Text = "Edit"
            AnswerDDListQ1.Visible = False
        End If

    End Sub

    Protected Sub ShowButQ1_Click(sender As Object, e As EventArgs) Handles ShowButQ1.Click
        If ShowButQ1.Text = "Show" Then
            ShowButQ1.Text = "Hide"

            RBListQ1.Visible = True
            AddButQ1.Visible = True
            EditQ1.Visible = True

            LQ11.Visible = False
            LQ12.Visible = False
            LQ13.Visible = False

            AnswerDDListQ1.Visible = False
            TextBoxQ1.Enabled = False
            TextBoxQ11.Enabled = False
            TextBoxQ11.Visible = False
            TextBoxQ12.Enabled = False
            TextBoxQ12.Visible = False
            If RBListQ1.Items.Count = 3 Then
                TextBoxQ13.Enabled = False
                TextBoxQ13.Visible = False
            End If
            EditQ1.Text = "Edit"

        Else

            ShowButQ1.Text = "Show"
            RBListQ1.Visible = False

            LQ11.Visible = False
            LQ12.Visible = False
            LQ13.Visible = False

            AddButQ1.Visible = False
            EditQ1.Visible = False

            AnswerDDListQ1.Visible = False
            TextBoxQ11.Visible = False
            TextBoxQ12.Visible = False
            TextBoxQ13.Visible = False
        End If
    End Sub


    Protected Sub AnswerDDListQ1_SelectedIndexChanged(sender As Object, e As EventArgs) Handles AnswerDDListQ1.SelectedIndexChanged
        LAnswerQ1.Text = AnswerDDListQ1.SelectedItem.Text
    End Sub

    Protected Sub CheckQ1_CheckedChanged(sender As Object, e As EventArgs) Handles CheckQ1.CheckedChanged
        If CheckQ1.Checked = True Then
            LBQuestion.Items.Add("2012W1 Final Question 1")
        Else
            LBQuestion.Items.Remove("2012W1 Final Question 1")

        End If
    End Sub

    Protected Sub CheckQ2_CheckedChanged(sender As Object, e As EventArgs) Handles CheckQ2.CheckedChanged

        If CheckQ2.Checked = True Then
            LBQuestion.Items.Add("2012W1 Final Question 2")
        Else
            LBQuestion.Items.Remove("2012W1 Final Question 2")

        End If
    End Sub

    Protected Sub LBQuestion_SelectedIndexChanged(sender As Object, e As EventArgs) Handles LBQuestion.SelectedIndexChanged
        SortStringListBox(LBQuestion)
    End Sub

    Private Shared Sub SortStringListBox(ByVal listBox As ListBox)
        Dim TempList As New List(Of String)
        For Each LI In listBox.Items
            TempList.Add(LI.ToString())
        Next
        TempList.Sort()
        listBox.DataSource = TempList
    End Sub
End Class
