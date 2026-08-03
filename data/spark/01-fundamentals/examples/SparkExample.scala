import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object SparkExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("SparkExample")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Create DataFrame from CSV
    val df = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data.csv")

    // Show data
    df.show()

    // Select columns
    df.select("name", "age").show()

    // Filter
    df.filter($"age" > 25).show()

    // Group by
    df.groupBy("department")
      .agg(avg("salary").as("avg_salary"))
      .show()

    // Join
    val departments = Seq(
      (1, "Engineering"),
      (2, "Marketing")
    ).toDF("dept_id", "dept_name")

    val result = df.join(departments, df("department_id") === departments("dept_id"))
    result.show()

    // Spark SQL
    df.createOrReplaceTempView("employees")
    spark.sql("SELECT * FROM employees WHERE age > 30").show()

    // Write to parquet
    df.write.parquet("output/employees.parquet")

    spark.stop()
  }
}
