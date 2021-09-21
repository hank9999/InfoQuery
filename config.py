import json


class Config:
    """配置类"""
    configfile = ''
    data = {}

    def __init__(self, filename: str):
        self.configfile = filename
        with open(self.configfile, 'r', encoding='utf8') as f:
            self.data = json.loads(f.read())

    def reload(self):
        with open(self.configfile, 'r', encoding='utf8') as f:
            self.data = json.loads(f.read())

    def get_token(self) -> str:
        return self.data['token']

