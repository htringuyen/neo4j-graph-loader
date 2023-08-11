WITH row
MERGE (state:State {code:row.STCODE})
    ON CREATE
    SET state.name = row.STNAME,
        state.abbrev = row.ABBREV,
        state.fips = row.STFIPS
MERGE (county:County {fips:row.FIPS})
    ON CREATE
    SET county.area = row.COUNTY,
        county.name = row.CTYNAME
MERGE (county)-[:STATE]->(state)
MERGE (city:City {name:trim(row.NAME)})-[:COUNTY]->(county)

FOREACH(ignore IN CASE WHEN row.ZIP IS NOT NULL AND row.ZIP <> '' THEN [1] ELSE [] END |
    MERGE (zip:ZipCode {code:row.ZIP})
        ON CREATE
        SET zip.location = point({latitude:toFloat(row.LAT), longitude:toFloat(row.LON)})
    SET county.ssa = row.CTYSSA
    MERGE (city)-[:HAS_ZIPCODE]->(zip)
)

