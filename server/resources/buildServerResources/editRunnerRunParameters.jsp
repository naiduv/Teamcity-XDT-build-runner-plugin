<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<%@ include file="xdtrunner.js" %>
<tr style="display:none">
    <th>
    </th>
    <td>
        <props:textProperty name="argument.configs_count" style="width:32em;"/>^M
    </td>
</tr>

<tr>
    <th>
        <label for="teamcity.build.workingDir">Working Directory: </label>
    </th>
    <td>
        <props:textProperty name="teamcity.build.workingDir" style="width:32em;"/>
        <span class="smallNote">
             Optional, specify if differs from the checkout directory.
        </span>
    </td>
</tr>

<% int server_configs_count = Integer.parseInt(propertiesBean.getProperties().get("argument.configs_count"));
   for(int i=0;i<server_configs_count;i++) {
    String input_path = "argument.input_path_" + i;
    String output_path = "argument.output_path_" + i;
    String xd_path = "argument.xd_path_" + i;
    String current_id = "configs_" + i;
%>
<tbody style="border-style: dotted; border-width: 1px " id=<%=current_id%>>
<tr>
    <th>
        <label for=<%= input_path %>>Input path: </label>
    </th>
    <td>
        <props:textProperty name="<%= input_path %>" style="width:32em;"/>
        <span class="smallNote">
             Source file to ctt.exe
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for=<%= xd_path %>>Transform file path: </label>
    </th>
    <td>
        <props:textProperty name="<%= xd_path %>" style="width:32em;"/>
        <span class="smallNote">
             Transformation file required for conversion.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for=<%= output_path %> >Output path: </label>
    </th>
    <td>
        <props:textProperty name="<%= output_path %>" style="width:32em;"/>
        <span class="smallNote">
             Destination file to ctt.exe
        </span>
    </td>
</tr>

</tbody>

<%}%>


<%-- HACK: save session on add/remove error because this jsp page on first load thru the runners dropdown will not load javascript, not sure why.. --%>
<%-- In the add case, we update the configs_count so it appears like it worked, and then reload. for error, just save session and reload --%>
<tr>
<th id="config-adder"><a style="color: #ff8c00; font-weight:normal" href="#" onclick="try{ addConfig(); } catch(err) {   document.getElementById('argument.configs_count').value = 2; BS.EditBuildRunnerForm.saveInSession(); location.reload() } return false;">Add transform group</a></th>
<td id="config-remover"><a style="color: #ff8c00; font-weight:normal" href="#" onclick="try{ removeConfig(); } catch(err) { BS.EditBuildRunnerForm.saveInSession(); location.reload() } return false;">Remove transform group</a></td>
</tr>

