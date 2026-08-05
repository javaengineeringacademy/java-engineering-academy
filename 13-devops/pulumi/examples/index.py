import pulumi
import pulumi_aws as aws

# Create an AWS S3 bucket
bucket = aws.s3.Bucket("my-bucket",
    website=aws.s3.BucketWebsiteArgs(
        index_document="index.html",
        error_document="error.html",
    ))

# Create an IAM role
role = aws.iam.Role("my-role",
    assume_role_policy=pulumi.Output.from_input({
        "Version": "2012-10-17",
        "Statement": [{
            "Action": "sts:AssumeRole",
            "Effect": "Allow",
            "Principal": {
                "Service": "ec2.amazonaws.com",
            },
        }],
    }).apply(lambda x: json.dumps(x)))

# Export the bucket name
pulumi.export("bucket_name", bucket.id)
pulumi.export("role_arn", role.arn)
