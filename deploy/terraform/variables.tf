  variable "aws_region" {
    description = "AWS Region"
    type        = string
    default     = "us-east-1"
  }

  variable "cluster_name" {
    description = "Nome do cluster EKS"
    type        = string
  }

  variable "namespace" {
    description = "Namespace do Kubernetes"
    type        = string
    default     = "fase5-video-notification-worker"
  }

  variable "service_account_name" {
    description = "Nome do Service Account"
    type        = string
    default     = "video-notification-worker-sa"
  }

  variable "sqs_queue_name" {
    description = "Nome da fila SQS"
    type        = string
    default     = "fase5-video-notification-queue"
  }

  variable "s3_bucket_name" {
    description = "Nome do bucket S3"
    type        = string
  }

  variable "dynamodb_table_name" {
    description = "Nome da tabela DynamoDB"
    type        = string
    default     = "fase5-video-processing"
  }

  variable "sns_topic_arn" {
    description = "ARN do topico SNS para notificacoes (opcional)"
    type        = string
    default     = ""
  }