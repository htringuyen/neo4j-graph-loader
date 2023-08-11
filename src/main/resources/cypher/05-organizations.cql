    WITH row
    MERGE (orgz:Organization {uuid:row.Id})
        ON CREATE
        SET orgz.name = row.NAME,
            orgz.phone = row.PHONE,
            orgz.revenue = toFloat(row.REVENUE),
            orgz.utilization = toInteger(row.UTILIZATION)
    
    WITH row, orgz
    CALL custom.snowj.synthea.createAddress(row.ADDRESS, row.CITY, row.STATE, row.ZIP, toFloat(row.LAT), toFloat(row.LON))
    YIELD node AS addr
    CREATE (orgz)-[:HAS_ADDRESS]->(addr)

