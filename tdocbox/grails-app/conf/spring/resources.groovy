// Place your Spring DSL code here
beans = {

  // A convenience bean for running native SQL
  groovySql(groovy.sql.Sql, ref('dataSource'))

}
