<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
    <th>
        <label for="teamcity.build.workingDir">Working Directory: </label>
    </th>
    <td>
        <props:textProperty name="teamcity.build.workingDir" style="width:32em;"/>
        <span class="error" id="error_teamcity.build.workingDir"></span>
        <span class="smallNote">
             Optional, specify if differs from the checkout directory.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.input_path">Input path: </label>
    </th>
    <td>
        <props:textProperty name="argument.input_path" style="width:32em;"/>
        <span class="error" id="error_argument.input_path"></span>
        <span class="smallNote">
             Input file to msxsl.exe
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.xsl_path">XSL file path: </label>
    </th>
    <td>
        <props:textProperty name="argument.xsl_path" style="width:32em;"/>
        <span class="error" id="error_argument.xsl_path"></span>
        <span class="smallNote">
             XSL file required for conversion.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.output_path">Output path: </label>
    </th>
    <td>
        <props:textProperty name="argument.output_path" style="width:32em;"/>
        <span class="error" id="error_argument.output_path"></span>
        <span class="smallNote">
             Output file to msxsl.exe
        </span>
    </td>
</tr>
