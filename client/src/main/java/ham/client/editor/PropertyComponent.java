package ham.client.editor;

import ham.client.WorkSpace;
import ham.client.userManagement.UserService;
import ham.shared.editor.Helper.Point;
import ham.shared.editor.abstracts.UMLDiagramObject;
import ham.shared.editor.abstracts.UMLNode;
import ham.shared.editor.classdiagram.ClassDiagramClass;
import ham.shared.editor.classdiagram.ClassDiagramEdge;
import ham.shared.editor.classdiagram.ClassDiagramNote;
import ham.shared.editor.classdiagram.ClassDiagramOperation;
import ham.shared.editor.classdiagram.ClassDiagramProperty;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class PropertyComponent implements Initializable {

    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private static final EditorService editorService = EditorService.getInstance();
    private static final UserService userService = UserService.getInstance();
    

    @FXML
    private TextField propertyNameTextField;
    @FXML
    private VBox positionVBox;
    @FXML
    private TextField propertyXTextField;
    @FXML
    private CheckBox multiplicityCheckBox;
    @FXML
    private CheckBox updateAllElemnetsCheckBox;
    @FXML
    private RadioButton abstractRadioButton;
    @FXML
    private CheckBox nameCheckBox;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private RadioButton normalRadioButton;
    @FXML
    private CheckBox roleNameCheckBox;
    @FXML
    private RadioButton interfaceRadioButton;
    @FXML
    private TextArea linesTextArea;
    @FXML
    private ComboBox<String> sourceMultiplicityComboBox;
    @FXML
    private ComboBox<String> targetMultiplicityComboBox;
    @FXML
    private TextField propertyYTextField;
    @FXML
    private TextField targetRoleTextField;
    @FXML
    private VBox propertyVBox;
    @FXML
    private TextField sourceRoleTextField;
    @FXML
    private ColorPicker borderColorPicker;
    @FXML
    private VBox noteVBox;
    @FXML
    private ColorPicker textColorPicker;

    @FXML
    private CheckBox sourceCheckBox;

    @FXML
    private CheckBox targetCheckBox;

    @FXML
    private TextField fontSizeTextField;

    @FXML
    private TextField sizeTextField;

    @FXML
    private TextArea propertyTextArea;

    @FXML
    private TextArea operationTextArea;

    @FXML
    private VBox classVBox;

    @FXML
    private VBox lastVBox;

    @FXML
    private VBox firstVBox;

    @FXML
    private VBox edgeVBox;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.loadUI();

        editorService.selected.addListener((obs, oldSelection, newSelection) ->
        {
            this.loadUI();
        });

        ObservableList<String> options
                = FXCollections.observableArrayList(
                        "1",
                        "*",
                        "0..1",
                        "1..*"
                );

        sourceMultiplicityComboBox.setItems(options);
        sourceMultiplicityComboBox.getSelectionModel().select(0);
        targetMultiplicityComboBox.setItems(options);
        targetMultiplicityComboBox.getSelectionModel().select(0);
    }

    @FXML
    void updateAction(ActionEvent event)
    {
         UMLDiagramObject  object = editorService.getSelectedObject();
        object.setName(propertyNameTextField.getText());
        object.setBackgroundColor(backgroundColorPicker.getValue().toString());
        object.setTextColor(textColorPicker.getValue().toString());

        try
        {
            object.setLineWidth(new Double(sizeTextField.getText()));
        } catch (Exception e)
        {
        }

        try
        {
            object.setFontSize(new Double(fontSizeTextField.getText()));
        } catch (Exception e)
        {
        }

        if (editorService.getSelectedObject().getClass() == ClassDiagramClass.class)
        {
            object.setBorderColor(borderColorPicker.getValue().toString());

            ClassDiagramClass class1 = (ClassDiagramClass) object;
            try
            {
                Double x = new Double(propertyXTextField.getText());
                Double y = new Double(propertyYTextField.getText());
                class1.setLocation(new Point(x, y));
            } catch (Exception e)
            {

            }

            if (normalRadioButton.isSelected())
            {
                class1.setTyp("NORMAL");
            }
            else if (abstractRadioButton.isSelected())
            {
                class1.setTyp("ABSTRACT");
            }
            else if (interfaceRadioButton.isSelected())
            {
                class1.setTyp("INTERFACE");
            }

            try
            {
                if (propertyTextArea.getText().equals(""))
                {
                    class1.getClassProperties().clear();
                    class1.calculateBound();
                }
                else
                {
                    ArrayList<ClassDiagramProperty> list = new ArrayList<>();
                    for (String part : propertyTextArea.getText().split("\n"))
                    {
                        String split[] = part.split(";");
                        list.add(new ClassDiagramProperty(split[0], split[1], split[2]));
                    }
                    class1.getClassProperties().clear();
                    for (ClassDiagramProperty property : list)
                    {
                        class1.addClassProperties(property);
                    }
                }
            } catch (Exception e)
            {
            }

            try
            {
                if (operationTextArea.getText().equals(""))
                {
                    class1.getClassOperations().clear();
                    class1.calculateBound();
                }
                else
                {
                    ArrayList<ClassDiagramOperation> list = new ArrayList<>();
                    for (String part : operationTextArea.getText().split("\n"))
                    {
                        String split[] = part.split(";");
                        ArrayList<String> parameterList = new ArrayList<>();
                        for (String parameter : split[2].split(","))
                        {
                            parameterList.add(parameter);
                        }
                        if (split[4].equals("a"))
                        {
                            list.add(new ClassDiagramOperation(split[0], split[1], parameterList, split[3], true));
                        }
                        else
                        {
                            list.add(new ClassDiagramOperation(split[0], split[1], parameterList, split[3], false));
                        }
                    }
                    class1.getClassOperations().clear();
                    for (ClassDiagramOperation operation : list)
                    {
                        class1.addClassOperations(operation);
                    }
                }
            } catch (Exception e)
            {
            }

        }
        else if (editorService.getSelectedObject().getClass() == ClassDiagramNote.class)
        {
            object.setBorderColor(borderColorPicker.getValue().toString());

            ClassDiagramNote note = (ClassDiagramNote) object;
            try
            {
                Double x = new Double(propertyXTextField.getText());
                Double y = new Double(propertyYTextField.getText());
                note.setLocation(new Point(x, y));
            } catch (Exception e)
            {

            }

            try
            {
                if (linesTextArea.getText().equals(""))
                {
                    note.setLines(new ArrayList<String>());
                }
                else
                {
                    ArrayList<String> list = new ArrayList<>();
                    String split[] = linesTextArea.getText().split("\n");

                    ArrayList<String> tmp = new ArrayList<>();
                    for (String strg : split)
                    {
                        tmp.add(strg);
                    }

                    note.setLines(tmp);
                }
            } catch (Exception e)
            {
            }

        }
        else if (editorService.getSelectedObject().getClass() == ClassDiagramEdge.class)
        {
            ClassDiagramEdge edge = (ClassDiagramEdge) object;

            edge.setSourceNavigable(sourceCheckBox.isSelected());
            edge.setTargetNavigable(targetCheckBox.isSelected());

            edge.setSourceRole(sourceRoleTextField.getText());
            edge.setTargetRole(targetRoleTextField.getText());

            edge.setHiddenMultiplicity(!multiplicityCheckBox.isSelected());
            edge.setHiddenName(!nameCheckBox.isSelected());
            edge.setHiddenRole(!roleNameCheckBox.isSelected());

            edge.setSourceMultiplicity(sourceMultiplicityComboBox.getSelectionModel().getSelectedItem());
            edge.setTargetMultiplicity(targetMultiplicityComboBox.getSelectionModel().getSelectedItem());

        }

        if(editorService.getSelectedObject().getClass().getSuperclass()==UMLNode.class&&updateAllElemnetsCheckBox.isSelected())
        {
            for(UMLDiagramObject element: editorService.getDiagram().getNodes())
            {
                try{element.setFontSize((new Double(fontSizeTextField.getText())).doubleValue());}catch(Exception e){ }
                try{element.setLineWidth((new Double(sizeTextField.getText())).doubleValue());}catch(Exception e){ }
                element.setBackgroundColor(backgroundColorPicker.getValue().toString());
                element.setBorderColor(borderColorPicker.getValue().toString());
                element.setTextColor(textColorPicker.getValue().toString());
            }
        }
        else if(editorService.getSelectedObject().getClass()==ClassDiagramEdge.class&&updateAllElemnetsCheckBox.isSelected())
        {
             for(UMLDiagramObject element: editorService.getDiagram().getEdges())
            {
                try{element.setFontSize((new Double(fontSizeTextField.getText())).doubleValue());}catch(Exception e){ }
                try{element.setLineWidth((new Double(sizeTextField.getText())).doubleValue());}catch(Exception e){ }
                element.setBackgroundColor(backgroundColorPicker.getValue().toString());
                element.setTextColor(textColorPicker.getValue().toString());
            }
        }
        
                if(editorService.getSelectedObject().getClass()==ClassDiagramClass.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Klasse) aktualisiert");
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramEdge.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Relation) aktualisiert"); 
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramNote.class)
                         editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Notiz) aktualisiert");
                
        editorService.renderCall.setValue(!editorService.renderCall.getValue());
        editorService.saveDiagram();
        editorService.loadUI = true;
    }

    private void loadUI()
    {
        
        UMLDiagramObject  object = editorService.getSelectedObject();
        if (object == null)
        {
            propertyVBox.setDisable(true);
        }
        else
        {
            propertyVBox.getChildren().clear();

            propertyVBox.setDisable(false);
            propertyNameTextField.setText(object.getName());

            sizeTextField.setText((new Double(object.getLineWidth())).toString());
            fontSizeTextField.setText((new Double(object.getFontSize())).toString());

            backgroundColorPicker.setValue(Color.web(object.getBackgroundColor()));
            textColorPicker.setValue(Color.web(object.getTextColor()));

            if (editorService.getSelectedObject().getClass() == ClassDiagramClass.class)
            {
                propertyVBox.getChildren().add(firstVBox);
                propertyVBox.getChildren().add(positionVBox);
                propertyVBox.getChildren().add(classVBox);
                propertyVBox.getChildren().add(lastVBox);

                borderColorPicker.setValue(Color.web(object.getBorderColor()));
                borderColorPicker.setDisable(false);

                ClassDiagramClass class1 = (ClassDiagramClass) object;
                propertyXTextField.setText((new Double(class1.getLocation().getX())).toString());
                propertyYTextField.setText((new Double(class1.getLocation().getY())).toString());
                switch (class1.getTyp())
                {
                    case "NORMAL":
                        normalRadioButton.setSelected(true);
                        break;
                    case "ABSTRACT":
                        abstractRadioButton.setSelected(true);
                        break;
                    case "INTERFACE":
                        interfaceRadioButton.setSelected(true);
                        break;
                }
                propertyTextArea.setText("");
                int count = 0;
                propertyTextArea.setPrefRowCount(class1.getClassProperties().size());
                for (ClassDiagramProperty property : class1.getClassProperties())
                {
                    count++;
                    propertyTextArea.setText(propertyTextArea.getText() + property.getAccessModifier() + ";" + property.getIdentifier() + ";" + property.getType());
                    if (class1.getClassProperties().size() > count)
                    {
                        propertyTextArea.setText(propertyTextArea.getText() + "\n");
                    }
                }

                operationTextArea.setText("");
                count = 0;
                int count2 = 0;
                operationTextArea.setPrefRowCount(class1.getClassOperations().size());
                for (ClassDiagramOperation operation : class1.getClassOperations())
                {
                    count++;
                    operationTextArea.setText(operationTextArea.getText() + operation.getAccessModifier() + ";" + operation.getIdentifier() + ";");

                    for (String parameter : operation.getParameterList())
                    {
                        count2++;
                        operationTextArea.setText(operationTextArea.getText() + parameter);
                        if (operation.getParameterList().size() > count)
                        {
                            operationTextArea.setText(operationTextArea.getText() + ",");
                        }
                    }

                    operationTextArea.setText(operationTextArea.getText() + ";" + operation.getReturnType() + ";");
                    if (operation.isAbstractMethod())
                    {
                        operationTextArea.setText(operationTextArea.getText() + "a");
                    }
                    else
                    {
                        operationTextArea.setText(operationTextArea.getText() + "n");
                    }
                    count2 = 0;
                    if (class1.getClassOperations().size() > count)
                    {
                        operationTextArea.setText(operationTextArea.getText() + "\n");
                    }
                }

            }
            else if (editorService.getSelectedObject().getClass() == ClassDiagramNote.class)
            {
                propertyVBox.getChildren().add(firstVBox);
                propertyVBox.getChildren().add(positionVBox);
                propertyVBox.getChildren().add(noteVBox);
                propertyVBox.getChildren().add(lastVBox);

                borderColorPicker.setValue(Color.web(object.getBorderColor()));
                borderColorPicker.setDisable(false);

                ClassDiagramNote note = (ClassDiagramNote) object;
                propertyXTextField.setText((new Double(note.getLocation().getX())).toString());
                propertyYTextField.setText((new Double(note.getLocation().getY())).toString());

                linesTextArea.setText("");
                int count = 0;
                linesTextArea.setPrefRowCount(note.getLines().size());
                for (String strg : note.getLines())
                {
                    count++;
                    linesTextArea.setText(linesTextArea.getText() + strg);
                    if (note.getLines().size() > count)
                    {
                        linesTextArea.setText(linesTextArea.getText() + "\n");
                    }
                }

            }
            else if (editorService.getSelectedObject().getClass() == ClassDiagramEdge.class)
            {

                propertyVBox.getChildren().add(firstVBox);
                propertyVBox.getChildren().add(edgeVBox);
                propertyVBox.getChildren().add(lastVBox);

                borderColorPicker.setValue(Color.TRANSPARENT);
                borderColorPicker.setDisable(true);

                ClassDiagramEdge edge = (ClassDiagramEdge) object;

                sourceCheckBox.setSelected(edge.isSourceNavigable());
                targetCheckBox.setSelected(edge.isTargetNavigable());

                if (!edge.getTyp().equals("ASSOCIATION"))
                {
                    sourceCheckBox.setDisable(true);
                    targetCheckBox.setDisable(true);
                }
                else
                {

                    sourceCheckBox.setDisable(false);
                    targetCheckBox.setDisable(false);
                }

                if (edge.getTyp().equals("REALIZATION"))
                {
                    sourceCheckBox.setDisable(false);
                }

                if (edge.getTyp().equals("GENERALIZATION") || edge.getTyp().equals("REALIZATION"))
                {
                    sourceMultiplicityComboBox.setDisable(true);
                    targetMultiplicityComboBox.setDisable(true);
                    roleNameCheckBox.setDisable(true);
                    multiplicityCheckBox.setDisable(true);
                    sourceRoleTextField.setDisable(true);
                    targetRoleTextField.setDisable(true);

                }
                else
                {
                    sourceMultiplicityComboBox.setDisable(false);
                    targetMultiplicityComboBox.setDisable(false);
                    roleNameCheckBox.setDisable(false);
                    multiplicityCheckBox.setDisable(false);
                    sourceRoleTextField.setDisable(false);
                    targetRoleTextField.setDisable(false);
                }

                sourceRoleTextField.setText(edge.getSourceRole());
                targetRoleTextField.setText(edge.getTargetRole());

                multiplicityCheckBox.setSelected(!edge.isHiddenMultiplicity());
                nameCheckBox.setSelected(!edge.isHiddenName());
                roleNameCheckBox.setSelected(!edge.isHiddenRole());

                for (String strg : sourceMultiplicityComboBox.getItems())
                {
                    if (edge.getSourceMultiplicity().equals(strg))
                    {
                        sourceMultiplicityComboBox.getSelectionModel().select(strg);
                    }
                }

                for (String strg : targetMultiplicityComboBox.getItems())
                {
                    if (edge.getTargetMultiplicity().equals(strg))
                    {
                        targetMultiplicityComboBox.getSelectionModel().select(strg);
                    }
                }

            }

        }

    }

}
