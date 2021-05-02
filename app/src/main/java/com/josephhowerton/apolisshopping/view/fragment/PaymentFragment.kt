package com.josephhowerton.apolisshopping.view.fragment

import android.content.DialogInterface
import com.josephhowerton.apolisshopping.model.Address
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.FragmentPaymentBinding
import com.josephhowerton.apolisshopping.interfaces.NetworkErrorListener
import com.josephhowerton.apolisshopping.model.AddressWithNameAndPhone
import com.josephhowerton.apolisshopping.model.orders.*
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.model.user.User
import com.josephhowerton.apolisshopping.viewmodel.PaymentViewModel

class PaymentFragment : Fragment(), NetworkErrorListener {
    private lateinit var mBinding: FragmentPaymentBinding
    private lateinit var mViewModel: PaymentViewModel
    private val mList:ArrayList<ProductLight> = ArrayList()
    private val paymentStatus = "Completed"
    private val paymentOnline = "Online By Card"
    private val paymentCash = "Cash"


    private lateinit var shippingSummary:ShippingInfo
    private lateinit var userSummary:UserInfo
    private lateinit var orderSummary: OrderInfo
    private lateinit var user: User

    private var deliveryCharges: Float = 5F
    private var discount: Float = 0F
    private var orderAmount: Float = 0F
    private var ourPrice: Float = 0F
    private  var totalAmount: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)
        mList.addAll(mViewModel.getUserShoppingCart())

        val address = requireArguments().get(Address.ADDRESS) as AddressWithNameAndPhone
        shippingSummary = ShippingInfo(address.pincode, address.city, address.houseNo, address.streetName)

        user = mViewModel.getUser()
        userSummary = UserInfo(user.email, user.mobile, user.firstName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)

        initData()
        initToolbar()
        initPaymentMethod()

        return mBinding.root
    }

    private fun initToolbar(){
        (requireActivity() as AppCompatActivity).setSupportActionBar(mBinding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Shopping Cart"
        setHasOptionsMenu(true)
    }

    private fun initData(){
        calculateTotal()
    }

    private fun calculateTotal(){
        for(product in mList){
            totalAmount = (totalAmount + (product.productPrice.toFloat() * product.productQuantity))
        }

        var taxes = 0.10F

        if(totalAmount < 100F){
            taxes = (totalAmount * taxes)
            discount = 0F
        }
        else{
            taxes = 0.05F
            taxes = (totalAmount * taxes)
            discount = ((totalAmount * .10F) - taxes) + deliveryCharges
            deliveryCharges = 0F
        }

        orderAmount = totalAmount + taxes + deliveryCharges
        ourPrice = orderAmount
        orderSummary(totalAmount, ourPrice, discount, deliveryCharges, orderAmount)
        displayOrder(totalAmount, taxes, deliveryCharges, orderAmount)
    }

    private fun orderSummary(totalAmount: Number, ourPrice: Number, discount: Number,
                             deliveryCharges: Number, orderAmount: Number){
        orderSummary = OrderInfo(totalAmount, ourPrice, discount, deliveryCharges, orderAmount)
    }

    private fun displayOrder(totalAmount: Number, taxes: Number,
                             deliveryCharges: Number, orderAmount: Number){
        mBinding.textViewAmount.text = String.format("$%.2f", orderAmount)
        mBinding.subTotalAmount.text = String.format("$%.2f", totalAmount)
        mBinding.taxAmount.text = String.format("$%.2f", taxes)
        mBinding.deliveryAmount.text = String.format("$%.2f", deliveryCharges)
        mBinding.orderAmount.text = String.format("$%.2f", orderAmount)
    }

    private fun initPaymentMethod(){
        mBinding.btnPayOnline.setOnClickListener {
            val order = makeOrder(paymentOnline)
            createOrder(order)
        }

        mBinding.btnPayCast.setOnClickListener {
            val order = makeOrder(paymentCash)
            createOrder(order)
        }
    }

    private fun makeOrder(paymentMode: String) : MakeOrder{
        return MakeOrder(user._id, orderSummary, userSummary, shippingSummary, PaymentInfo(paymentMode, paymentStatus), products())
    }

    private fun products() : ArrayList<ProductsInfo>{
        val productInfoList = ArrayList<ProductsInfo>()
        for(product in mList){
            productInfoList.add(ProductsInfo(product.productPrice, product.productPrice, product.productQuantity, product.productImage))
        }

        return productInfoList
    }



    private fun createOrder(order: MakeOrder){
        mViewModel.createOrderAndSaveInDatabase(order, this).observe(viewLifecycleOwner, {
            if(it){
                initPaidScreen()
            }
        })
    }

    private fun initPaidScreen(){
        mBinding.btnPayOnline.visibility =  View.GONE
        mBinding.btnPayCast.visibility = View.GONE
        mBinding.orderConfirmed.layout.visibility = View.VISIBLE
        mBinding.orderConfirmed.goHome.setOnClickListener{
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_main)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> requireActivity().onBackPressed()
        }
        return true
    }

    override fun onNetworkError(message: String) {
        alertUser(message)
    }

    private fun alertUser(message:String) {
        AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setTitle(Config.TITLE)
                .setMessage(message)
                .setPositiveButton(
                        Config.BTN,
                ) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }.show()
    }
}