WITH row
MERGE (p:Provider {id: row.Id})
SET p.name=row.NAME,
p.gender=row.GENDER,
p.address = row.ADDRESS,
p.state = row.STATE,
p.location = point({latitude:toFloat(row.LAT),
                    longitude:toFloat(row.LON)})
WITH row,p
MERGE (o:Organization {id: row.ORGANIZATION})
MERGE(p)-[:BELONGS_TO]->(o)
WITH row,p
MERGE (s:Specialty {name: row.SPECIALITY})
MERGE (p)-[:HAS_SPECIALTY]->(s)
WITH row,p
  WHERE row.ZIP IS NOT NULL
MERGE (z:ZipCode {zip: row.ZIP})
MERGE (p)-[:HAS_ZIPCODE]->(z)
