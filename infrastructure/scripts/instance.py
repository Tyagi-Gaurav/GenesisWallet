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

def terminate_ec2_instances(client, response):
    for reservation in response["Reservations"]:
        for instance in reservation[0]["Instances"]:
            print(instance["InstanceId"])
            client.terminate_instances(InstanceIds=[instance["InstanceId"]])

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

