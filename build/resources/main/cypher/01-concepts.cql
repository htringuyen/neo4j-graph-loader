    WITH row
    WITH row, CASE WHEN row.SYSTEM = "http://hl7.org/fhir/sid/cvx"
        THEN "CVX" ELSE row.SYSTEM END AS codeSystem 
    CALL apoc.merge.node(["Code", codeSystem], {identifier:row.CODE}, {description:row.DESCRIPTION}, {})
    YIELD node AS code
    RETURN code

