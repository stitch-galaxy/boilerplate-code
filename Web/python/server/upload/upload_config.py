serverName = "localhost"
serverPort = 8051

folderToStoreData = "./data/"

updateSql = """select d.DesignId, d.ReleaseDate, p.Width, p.Heigth, p.Colors, p.Name, p.Description, p.ImageSmall, p.ImageLargeName, p.DescriptionUrl, p.DownloadUrl, dc.Sales, dc.AvgRating
from T_DESIGNS d
INNER JOIN T_DESIGN_LOCALIZATION dl on dl.DesignId = d.DesignId
INNER JOIN T_PARAMETERS p on p.ParametersId = dl.ParametersId
LEFT OUTER JOIN T_DESIGN_COUNTERS dc on d.DesignId = dc.DesignId
WHERE dl.Language = %s"""

dbConfig = {
  "user": "root",
  "password": "root",
  "host": "127.0.0.1",
  "database": "stitchgalaxy",
  "raise_on_warnings": True,
}
