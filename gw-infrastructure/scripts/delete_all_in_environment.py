import boto3
import vpc
import instance
import ecs

def detach_internet_gateway_from(client, igws, vpcid):
     # detach and delete all gateways associated with the vpc
    if igws["InternetGateways"]:
        for gw in igws["InternetGateways"]:
            client.detach_internet_gateway(DryRun=False,
                                           InternetGatewayId=gw["InternetGatewayId"],
                                           VpcId=vpcid)

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

def main(argv=None):
    env = "dev"
    ec2_client = boto3.client("ec2")
    ecs_client = boto3.client("ecs")
    elbv2_client = client = boto3.client("elbv2")

    vpc.get_vpc_for(ec2_client, env)
    #vpcid = vpc_id_from(vpc)

    ec2_instances_list = instance.describe_ec2_instances(ec2_client, env)
    instance.terminate_ec2_instances(ec2_client, ec2_instances_list)

    ecs_clusters_list = ecs.list_ecs_clusters(ecs_client)
    ecs.delete_ecs_clusters(ecs_client, ecs_clusters_list)

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