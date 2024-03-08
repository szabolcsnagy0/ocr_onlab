# !pip install -q pandas
# !pip install -q autotrain-advanced safetensors
# !pip install huggingface_hub
# !pip install accelerate
# !pip install -i https://pypi.org/simple/ bitsandbytes
#
# !autotrain setup --update-torch

from huggingface_hub import notebook_login

notebook_login()

# !autotrain
# llm --train
# --project-name 'llama2-names'
# --model abhishek/llama-2-7b-hf-small-shards
# --data-path .
# --peft
# --quantization int4
# --lr 2e-4
# --text_column text
# --batch-size 2
# --epochs 3
# --model_max_length 2048
# --trainer sft
# --block_size 2048
# --push-to-hub
# --repo-id szabolcsnagy/names_top
