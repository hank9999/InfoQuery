from config import Config
from bot import BotMain

if __name__ == '__main__':
    conf = Config('BotConfig.json')
    token = conf.get_token()
    bot = BotMain(token)
    bot.init()
    bot.get_self_id()
    bot.register_default_function()
    bot.run()
