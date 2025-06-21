function comprar(produto, valor) {
    const cliente = {
      name: "João Cliente",
      cellphone: "(11) 99999-9999",
      email: "joao@cliente.com",
      taxId: "987.654.321-00"
    };

    fetch('http://localhost:8080/api/pagar', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ produto, valor, cliente })
    })
    .then(res => res.json())
    .then(dados => {
      console.log("Checkout criado:", dados);
      const urlCheckout = dados?.data?.checkoutUrl;
      if (urlCheckout) {
        window.location.href = urlCheckout;
      } else {
        alert("Erro ao obter URL do pagamento.");
      }
    })
    .catch(err => {
      console.error("Erro:", err);
      alert("Erro ao conectar com a API.");
    });
  }