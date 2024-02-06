def delete_ecs_clusters(ecs_client, response):
    for cluster in response["clusterArns"]:
        print("Deleting Cluster: " + cluster)
        ecs_client.delete_cluster(cluster=cluster)


def list_ecs_clusters(ecs_client):
    response = ecs_client.list_clusters()
    print(response["clusterArns"])
    return response
