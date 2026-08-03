package main

import (
    "github.com/pulumi/pulumi-aws/sdk/v5/go/aws/s3"
    "github.com/pulumi/pulumi-aws/sdk/v5/go/aws/iam"
    "github.com/pulumi/pulumi/sdk/v3/go/pulumi"
)

func main() {
    pulumi.Run(func(ctx *pulumi.Context) error {
        // Create an AWS S3 bucket
        bucket, err := s3.NewBucket(ctx, "my-bucket", &s3.BucketArgs{
            Website: &s3.BucketWebsiteArgs{
                IndexDocument: pulumi.String("index.html"),
                ErrorDocument: pulumi.String("error.html"),
            },
        })
        if err != nil {
            return err
        }

        // Create an IAM role
        role, err := iam.NewRole(ctx, "my-role", &iam.RoleArgs{
            AssumeRolePolicy: pulumi.String(`{
                "Version": "2012-10-17",
                "Statement": [{
                    "Action": "sts:AssumeRole",
                    "Effect": "Allow",
                    "Principal": {
                        "Service": "ec2.amazonaws.com"
                    }
                }]
            }`),
        })
        if err != nil {
            return err
        }

        // Export the bucket name
        ctx.Export("bucketName", bucket.ID())
        ctx.Export("roleArn", role.Arn)

        return nil
    })
}
