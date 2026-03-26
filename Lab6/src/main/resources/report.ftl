<html>
<head>
    <title>Movie Report</title>
    <style>
        table { border-collapse: collapse; width: 100%; font-family: sans-serif; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>Lab 6: Movie Database Report</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Release Date</th>
            <th>Duration</th>
            <th>Score</th>
            <th>Genre</th>
        </tr>
        <#list movies as movie>
        <tr>
            <td>${movie.id}</td>
            <td>${movie.title}</td>
            <td>${movie.releaseDate}</td>
            <td>${movie.duration} min</td>
            <td>${movie.score}/10</td>
            <td>${movie.genreName!"Unknown"}</td>
        </tr>
        </#list>
    </table>
</body>
</html>
