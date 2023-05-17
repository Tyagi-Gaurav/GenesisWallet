import sys
import boto3


def internet_gateway_cleanup():
    #Unmap public IP addresses
    client = boto3.client('ec2')
    addresses_dict = client.describe_addresses()
    print(addresses_dict)
    for eip_dict in addresses_dict['Addresses']:
        print(eip_dict['PublicIp'])


def vpc_cleanup(vpcid):
    """Remove VPC from AWS
    Set your region/access-key/secret-key from env variables or boto config.
    :param vpcid: id of vpc to delete
    """
    if not vpcid:
        print ("Nothing to delete")
        return
    print('Removing VPC ({}) from AWS'.format(vpcid))
    ec2 = boto3.resource('ec2')
    ec2client = ec2.meta.client
    vpc = ec2.Vpc(vpcid)
    # detach default dhcp_options if associated with the vpc
    dhcp_options_default = ec2.DhcpOptions('default')
    if dhcp_options_default:
        dhcp_options_default.associate_with_vpc(
            VpcId=vpc.id
        )
    # detach and delete all gateways associated with the vpc
    for gw in vpc.internet_gateways.all():
        vpc.detach_internet_gateway(InternetGatewayId=gw.id)
        gw.delete()
    # delete all route table associations
    for rt in vpc.route_tables.all():
        if not rt.associations:
            rt.delete()
        for rta in rt.associations:
            if not rta.main:
                rta.delete()
    # delete any instances
    for subnet in vpc.subnets.all():
        for instance in subnet.instances.all():
            instance.terminate()
    # delete our endpoints
    for ep in ec2client.describe_vpc_endpoints(
            Filters=[{
                'Name': 'vpc-id',
                'Values': [vpcid]
            }])['VpcEndpoints']:
        ec2client.delete_vpc_endpoints(VpcEndpointIds=[ep['VpcEndpointId']])
    # delete our security groups
    for sg in vpc.security_groups.all():
        if sg.group_name != 'default':
            sg.delete()
    # delete any vpc peering connections
    for vpcpeer in ec2client.describe_vpc_peering_connections(
            Filters=[{
                'Name': 'requester-vpc-info.vpc-id',
                'Values': [vpcid]
            }])['VpcPeeringConnections']:
        ec2.VpcPeeringConnection(vpcpeer['VpcPeeringConnectionId']).delete()
    # delete non-default network acls
    for netacl in vpc.network_acls.all():
        if not netacl.is_default:
            netacl.delete()
    # delete network interfaces
    for subnet in vpc.subnets.all():
        for interface in subnet.network_interfaces.all():
            interface.delete()
        subnet.delete()
    # finally, delete the vpc
    ec2client.delete_vpc(VpcId=vpcid)


def main(argv=None):
    internet_gateway_cleanup()
    #vpc_cleanup("vpc-040467649d9c2302b")


if __name__ == '__main__':
    main("")