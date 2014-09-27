<script type="text/javascript">
    var configs_count = 1;

    function addConfig(){
        configs_count = parseInt(document.getElementById("argument.configs_count").value);

        if(configs_count == 10)
            return false;

        var configs = document.getElementById("configs"+"_"+(configs_count-1));
        var copy = configs.outerHTML.replace(new RegExp("_"+(configs_count-1), 'g'), "_"+configs_count);
        configs.insertAdjacentHTML('afterend', copy)

        configs_count += 1;
        document.getElementById("argument.configs_count").value=configs_count;
        console.log("config_count_from_element after add = " +  document.getElementById("argument.configs_count").value);

        document.getElementById("config-remover").show();

        return false;
    };

    function removeConfig(){
        configs_count = parseInt(document.getElementById("argument.configs_count").value);

        if(configs_count == 1)
            return false;

        var configs = document.getElementById("configs_" + (configs_count-1));
        configs.remove();

        configs_count -= 1;
        document.getElementById("argument.configs_count").value=configs_count;
        console.log("config_count_from_element after remove = " + document.getElementById("argument.configs_count").value);

        if(configs_count == 1){
            document.getElementById("config-remover").hide();
        }

        return false;
    };
</script>