import boto3

def ec2_instance_cleanup(client):
    response = client.describe_instances(Filters=[
        {
            'Name': 'instance-state-code',
            'Values': [
                '0','16','32' #Get instances that are pending, running or shutting-down
                #'48' #Get instances that are pending, running or shutting-down
            ]
        },
    ])
    if response['Reservations']:
        instances = response['Reservations'][0]['Instances']
        for instance in instances:
            print ("Deleting instanceID : " + instance["InstanceId"])
            client.terminate_instances(InstanceIds=[instance["InstanceId"]])
    else:
        print ("No EC2 Instances found to delete.")

def detach_internet_gateway_from(client, igws, vpcid):
     # detach and delete all gateways associated with the vpc
    if igws["InternetGateways"]:
        for gw in igws["InternetGateways"]:
            client.detach_internet_gateway(DryRun=False,
                                           InternetGatewayId=gw["InternetGatewayId"],
                                           VpcId=vpcid)

def get_vpc_for(client, env):
    print ("Get VPCs")
    response = client.describe_vpcs(
        Filters=[
            {
                'Name': 'tag:Environment',
                'Values': [
                    env,
                ]
            },
        ])
    print (response)
    if (response["Vpcs"]):
        return response
    else:
        print ("No Vpcs Found")
        return None

def vpc_id_from(vpcs):
    return vpcs["Vpcs"][0]["VpcId"]

def get_internet_gateways(client):
    print ("Get Internet Gateways")
    return client.describe_internet_gateways()

def get_nat_gateways(client):
    print ("Get NAT Gateways")
    return client.describe_nat_gateways()

def get_network_interfaces(client):
    print ("Get Network interfaces")
    interfaces = client.describe_network_interfaces()
    print (interfaces)
    return interfaces

def detach_network_interfaces(client, nwifs):
    print ("Detach Network interfaces")
    for nw in nwifs["NetworkInterfaces"]:
        if nw["Attachment"]["Status"] == 'attached':
            client.detach_network_interface(AttachmentId=nw["Attachment"]['AttachmentId'])

def describe_load_balancers(client):
    print ("Describe load balancers")
    return client.describe_load_balancers()

def delete_load_balancers(client, lobs):
    print ("Deleting load balancers")
    for nw in lobs["LoadBalancers"]:
        print ("Deleting loadbalancer with arn " + nw["LoadBalancerArn"])
        client.delete_load_balancer(LoadBalancerArn=nw["LoadBalancerArn"])

def delete_ecs_clusters(ecs_client, response):
    for cluster in response["clusterArns"]:
        print("Deleting Cluster: " + cluster)
        ecs_client.delete_cluster(cluster=cluster)


def list_ecs_clusters(ecs_client):
    response = ecs_client.list_clusters()
    print(response["clusterArns"])
    return response


def terminate_ec2_instances(client, response):
    for reservation in response["Reservations"]:
        for instance in reservation[0]["Instances"]:
            print(instance["InstanceId"])
            client.terminate_instances(InstanceIds=[instance["InstanceId"]])


def describe_ec2_instances(client, env):
    print ("Get ECNs")
    response = client.describe_instances(Filters=[
        {
            'Name': 'tag:Environment',
            'Values': [
                env,
            ]
        },
    ])
    print (response)
    return response

def main(argv=None):
    env = "dev"
    ec2_client = boto3.client("ec2")
    ecs_client = boto3.client("ecs")
    elbv2_client = client = boto3.client("elbv2")

    vpc = get_vpc_for(ec2_client, env)
    #vpcid = vpc_id_from(vpc)

    ec2_instances_list = describe_ec2_instances(ec2_client, env)
    terminate_ec2_instances(ec2_client, ec2_instances_list)

    ecs_clusters_list = list_ecs_clusters(ecs_client)
    delete_ecs_clusters(ecs_client, ecs_clusters_list)

    nwif = get_network_interfaces(ec2_client)
    detach_network_interfaces(ec2_client, nwif)

    # response = client.terminate_instances(
    #     InstanceIds=[
    #         'string',
    #     ],
    #     DryRun=True|False
    # )

    # natgws = get_nat_gateways()
    # print(natgws)
    #
    # lobs = describe_load_balancers(elbv2_client)
    # print (lobs)
    # delete_load_balancers(elbv2_client, lobs)
    #
    # client = boto3.client("ec2")
    # print ("Get routing tables")
    # response = client.describe_route_tables()
    # print (response)
    # for rt in response["RouteTables"]:
    #     print ("Deleting routing table with ID: " + rt["RouteTableId"])
    #     client.delete_route_table(RouteTableId=rt["RouteTableId"])


    # igws = get_internet_gateways(ec2_client)
    # print(igws)
    # detach_internet_gateway_from(igws, vpcid)
    #ec2_instance_cleanup()


if __name__ == '__main__':
    main("")