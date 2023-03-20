module "vault_instance" {
  source                  = "../modules/vault"
  ENV                     = var.ENV
  SUBNET_ID               = element(module.main-vpc.public_subnets, 0)
  VPC_ID                  = module.main-vpc.vpc_id
  DB_HOST                 = module.single_db_instance.host
  DB_PORT                 = module.single_db_instance.port
  DB_USER                 = module.single_db_instance.username
  DB_PASSWORD             = module.single_db_instance.password
  ALLOWED_SECURITY_GROUPS = {
    group1 = module.allow_cluster_access.ssh_security_group_id,
    group2 = module.dev-user-ecs-cluster.cluster_sg_id
  }
  ACCESS_KEY_NAME = module.allow_cluster_access.ssh_key_pair_name
}