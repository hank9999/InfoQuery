import json
from datetime import datetime

from khl import Bot, Message
import requests


class BotMain:
    """机器人类"""
    token = ''
    bot = None
    id = None

    def __init__(self, token: str):
        self.token = token

    def init(self):
        self.bot = Bot(token=self.token)

    def register_default_function(self):
        @self.bot.command(regex=r'.*@(.*)#' + str(self.id) + r'.*')
        async def info(msg: Message):
            text = f"消息类型: 文字消息\n" \
                   f"消息ID: {msg.msg_id}\n" \
                   f"发送人: {msg.author.username}#{msg.author.identify_num}: {msg.author.id}\n" \
                   f"服务器: {msg.ctx.guild.id}\n" \
                   f"频道: {msg.extra['channel_name']}#{msg.ctx.channel.id}\n" \
                   f"引用用户: "

            mention = list(dict.fromkeys(msg.extra['mention']))
            for i in mention:
                content = msg.content
                id_index = content.find(i) - 1
                content = content[:id_index]
                at_index = content.rfind('@') + 1
                username = content[at_index:id_index]
                text += f'{username}#{i}, '
            text = text[:-2] + '\n'
            if msg.extra['mention_roles']:
                text += '引用角色: '
                mention_roles = list(dict.fromkeys(msg.extra['mention_roles']))
                role_list = self.get_role_list(msg.ctx.guild.id)
                for i in role_list:
                    if i['role_id'] in mention_roles:
                        text += f"{i['name']}#{i['role_id']}, "
                text = text[:-2] + '\n'
            if text.endswith('\n'):
                text = text[:-1]
            print('[{}] {}'.format(
                datetime.now().strftime('%m-%d %H:%M:%S'),
                text.replace('\n', '; ')
            ))
            await msg.reply(text)

    def run(self):
        if self.bot is None or self.id is None:
            raise Exception('Must run after init')
        self.bot.run()

    def get_self_id(self):
        self.id = json.loads(
            requests.get('https://www.kaiheila.cn/api/v3/user/me', headers={'Authorization': f'Bot {self.token}'}).text
        )['data']['id']

    def get_role_list(self, guild_id: str) -> list:
        return json.loads(
            requests.get('https://www.kaiheila.cn/api/v3/guild-role/list',
                         headers={'Authorization': f'Bot {self.token}'},
                         params={'guild_id': guild_id}
                         ).text
        )['data']['items']
