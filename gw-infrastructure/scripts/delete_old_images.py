import boto3

def list_untagged_images(accountId, repositoryName):
    client = boto3.client("ecr")
    response = client.list_images(
        registryId=accountId,
        repositoryName=repositoryName,
        filter = {
            'tagStatus' : "UNTAGGED"
        }
    )

    iterator = map(untagged_image, response["imageIds"])

    response2 = client.batch_delete_image(
        registryId=accountId,
        repositoryName=repositoryName,
        imageIds= list(iterator)
    )

    print(response2)

def untagged_image(imageId):
    return {"imageDigest" : imageId["imageDigest"]}

def main():
    list_untagged_images("<accountId>", "test_genesis/gw-user")
    list_untagged_images("<accountId>", "test_genesis/gw-ui")
    list_untagged_images("<accountId>", "test_genesis/gw-api-gateway")

if __name__ == "__main__":
    main()