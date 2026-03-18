<!DOCTYPE html>
<html>
<head>
    <title>Bibliography Report</title>
    <style>
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; border-radius: 4px;}
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>Catalog Resources</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Location</th>
            <th>Author</th>
            <th>Year</th>
        </tr>
        <#list items as resource>
        <tr>
            <td>${resource.id}</td>
            <td>${resource.title!"N/A"}</td>
            <td>${resource.location!"N/A"}</td>
            <td>${resource.author!"N/A"}</td>
            <td>${resource.year!"N/A"}</td>
        </tr>
        </#list>
    </table>
</body>
</html>
