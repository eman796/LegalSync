package com.development.legally.ui.cases

import android.graphics.Matrix
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.PathParser
import com.development.legally.R
import com.development.legally.ui.theme.LegallyTheme

private val FigmaBackground = Color(0xFF181E27)
private val FigmaGold = Color(0xFF9E8D44)
private val FigmaTextWhite = Color(0xFFFFFFFF)

@Composable
fun NewCaseScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = FigmaBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 1.1 Barra Superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = FigmaGold,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() }
                )

                Text(
                    text = stringResource(id = R.string.new_case_title),
                    color = FigmaTextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                )

                Text(
                    text = stringResource(id = R.string.cancel),
                    color = FigmaGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .width(82.dp)
                        .clickable { onCancelClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Titulo Informacion General
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(29.dp)
            ) {
                GeneralInfoIcon(modifier = Modifier.size(23.dp))
                Spacer(modifier = Modifier.width(11.dp))
                Text(
                    text = stringResource(id = R.string.general_info),
                    color = FigmaTextWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            // 3. Numero Expediente
            InputField(
                label = stringResource(id = R.string.case_number_label),
                value = "25-00000-033-PE",
                fieldWidth = 355.dp,
                fieldHeight = 26.dp
            )

            Spacer(modifier = Modifier.height(19.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                // 4. Tipo Proceso
                InputField(
                    label = stringResource(id = R.string.process_type_label),
                    value = "Penal",
                    fieldWidth = 158.dp,
                    fieldHeight = 28.dp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(38.dp))
                // 5. Estado del caso
                InputField(
                    label = stringResource(id = R.string.case_status_label),
                    value = "Activo",
                    fieldWidth = 158.dp,
                    fieldHeight = 28.dp,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // 6. Descripcion del caso
            InputField(
                label = stringResource(id = R.string.case_description_label),
                value = "",
                fieldWidth = 355.dp,
                fieldHeight = 348.dp,
                isMultiline = true
            )
            Text(
                text = stringResource(id = R.string.char_count, 0, 1000),
                color = FigmaTextWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 7. Seccion Cliente
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(23.dp)
            ) {
                ClientSectionIcon(modifier = Modifier.size(23.dp))
                Spacer(modifier = Modifier.width(11.dp))
                Text(
                    text = stringResource(id = R.string.client_section),
                    color = FigmaTextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 8. Informacion de cliente
            ClientItem(
                name = stringResource(id = R.string.participant_name),
                id = stringResource(id = R.string.id_label, "5-0456-0691")
            )

            Spacer(modifier = Modifier.height(81.dp))

            // 13. Boton Guardar
            Button(
                onClick = { onSaveClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FigmaGold),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SaveIcon(modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.save_case),
                        color = FigmaTextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    fieldWidth: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.Dp.Unspecified,
    fieldHeight: androidx.compose.ui.unit.Dp = 26.dp,
    isMultiline: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = FigmaTextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.height(15.dp)
        )
        Spacer(modifier = Modifier.height(11.dp))
        Box(
            modifier = Modifier
                .then(if (fieldWidth != androidx.compose.ui.unit.Dp.Unspecified) Modifier.width(fieldWidth) else Modifier.fillMaxWidth())
                .height(fieldHeight)
                .background(FigmaBackground, RoundedCornerShape(12.dp))
                .border(1.dp, FigmaTextWhite, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = if (isMultiline) Alignment.TopStart else Alignment.CenterStart
        ) {
            Text(
                text = value,
                color = FigmaTextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                modifier = if (isMultiline) Modifier.padding(top = 8.dp) else Modifier
            )
        }
    }
}

@Composable
private fun ClientItem(
    name: String,
    id: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(FigmaGold)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = FigmaTextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = id,
                color = FigmaTextWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal
            )
        }

        PlusIcon(
            modifier = Modifier
                .size(25.dp)
                .clickable { }
        )
    }
}

@Composable
private fun GeneralInfoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val pathData = "M 0 19.642505787037038 L 0 3.357494212962963 C 0 2.5155505648365724 0.28165549489430775 1.6815801108324968 0.8214592595359957 1.044849537037037 C 1.3652661888939739 0.40341629584630323 2.1449085565802806 0 3.0039974595584114 0 L 7.177457316845369 0 C 7.710002729719664 0.00010347807848895038 8.223339664979266 0.1572198602888319 8.668285575522098 0.4400679976851852 C 9.108602594134831 0.7200726712191546 9.465531116633384 1.1118721564610798 9.720887660178578 1.5664424189814814 L 10.5810139350693 3.0696614583333335 L 10.585310270108716 3.0771484375 C 10.71763835956435 3.3141962024900646 10.887815288413016 3.489425754657498 11.065640527515225 3.6012369791666665 C 11.241587064056619 3.711761011017693 11.42343715408775 3.760448774219387 11.595808271378937 3.7584635416666665 L 19.996002540441587 3.7584635416666665 C 20.855105656149156 3.7584635416666665 21.63473239507763 4.161862523467453 22.178540740464005 4.803313078703704 C 22.718270084946745 5.439956219108017 22.99992343412116 6.273297325328544 23 7.115125868055555 L 23 19.642505787037038 C 23 20.484451364587855 22.718343351304778 21.318422529432507 22.178540740464005 21.955150462962962 C 21.63474073391151 22.596499266447843 20.85502719768701 23 19.996002540441587 23 L 3.0039974595584114 23 C 2.1449083467982963 23 1.3652659266664933 22.596583754928023 0.8214592595359957 21.955150462962962 C 0.28165559978529997 21.318420904654044 0 20.484449739809392 0 19.642505787037038 Z"
        val path = PathParser.createPathFromPathData(pathData)
        val rectF = RectF()
        path.computeBounds(rectF, true)
        val matrix = Matrix()
        val scale = minOf(size.width / rectF.width(), size.height / rectF.height())
        matrix.setScale(scale, scale)
        path.transform(matrix)
        drawPath(path = path.asComposePath(), color = FigmaGold, style = Fill)
    }
}

@Composable
private fun ClientSectionIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val pathData = "M 4.9725847244262695 0.007834897376596928 C 4.943841461092234 0.013124099001288414 4.854018792510033 0.031636305153369904 4.774974822998047 0.04750391095876694 C 3.7150670289993286 0.2537827864289284 2.7359994649887085 1.269309639930725 2.227602958679199 2.689460277557373 C 1.6581270694732666 4.278865337371826 1.7012419700622559 6.1644651889801025 2.3407795429229736 7.7009782791137695 C 2.793485939502716 8.79055380821228 3.5695539712905884 9.628893077373505 4.415683746337891 9.946245193481445 C 4.875575959682465 10.118144258856773 5.484573394060135 10.139300644397736 5.928297519683838 10.004426002502441 C 6.465437233448029 9.837816148996353 6.97922357916832 9.488729119300842 7.383425712585449 9.015345573425293 C 7.460673235356808 8.92278454452753 7.47684115357697 8.888404212892056 7.487619876861572 8.793198585510254 C 7.613371655344963 7.574037551879883 7.947512447834015 6.54264372587204 8.520581245422363 5.601165771484375 C 8.703819543123245 5.2996812760829926 8.7074118219316 5.278524696826935 8.68226146697998 4.717869281768799 C 8.599624581634998 2.916896104812622 7.884636282920837 1.3327799439430237 6.787003040313721 0.5261766910552979 C 6.312739223241806 0.1770893931388855 5.879793763160706 0.02370250318199396 5.331875324249268 0.005190297029912472 C 5.164805099368095 -0.00009890459477901459 5.003124440088868 0.0025456957519054413 4.9725847244262695 0.007834897376596928 Z M 17.45793914794922 0.007833586074411869 C 17.429195884615183 0.013122787699103355 17.3393741697073 0.031634993851184845 17.260330200195312 0.04750259965658188 C 16.57947415113449 0.17973264306783676 15.889636099338531 0.6742730140686035 15.366868019104004 1.406827449798584 C 14.87463966012001 2.0970683097839355 14.50816197693348 3.088793456554413 14.377020835876465 4.077874183654785 C 14.335702393203974 4.405804693698883 14.305163623765111 4.9849721640348434 14.319535255432129 5.201829433441162 C 14.328517525456846 5.336704075336456 14.337499991059303 5.360506027936935 14.474030494689941 5.585297107696533 C 14.664454609155655 5.897359997034073 14.781223438680172 6.116861581802368 14.903382301330566 6.391900062561035 C 15.199797213077545 7.068917870521545 15.413575388491154 7.864942848682404 15.498008728027344 8.626587867736816 C 15.52675199136138 8.875180333852768 15.532141543924809 8.896336480975151 15.595017433166504 8.980963706970215 C 15.711786933243275 9.136995151638985 16.0315560400486 9.43319134414196 16.254316329956055 9.594511985778809 C 16.511209219694138 9.776989445090294 16.77887988090515 9.911864019930363 17.077091217041016 10.004425048828125 C 17.37709903717041 10.096986077725887 17.862143218517303 10.11814260110259 18.185504913330078 10.057316780090332 C 18.974148154258728 9.90392991900444 19.660391807556152 9.390876591205597 20.215496063232422 8.544604301452637 C 21.433491826057434 6.682805180549622 21.501756191253662 3.7949020862579346 20.37537956237793 1.8035175800323486 C 19.852611482143402 0.8805518746376038 19.11247408390045 0.26171526685357094 18.293291091918945 0.05808100476861 C 18.12981379032135 0.018411990255117416 17.571115747094154 -0.015967820771038532 17.45793914794922 0.007833586074411869 Z M 11.035616874694824 4.635885238647461 C 9.66671907901764 4.910923719406128 8.525971084833145 6.4077675342559814 8.179255485534668 8.391218185424805 C 7.658283889293671 11.363749504089355 9.06131362915039 14.330991506576538 11.14699649810791 14.666855812072754 C 12.986565232276917 14.963051110506058 14.67882515490055 12.98488974571228 14.906974792480469 10.271529197692871 C 14.991408124566078 9.258647084236145 14.878231436014175 8.290723860263824 14.562055587768555 7.373047351837158 C 14.048269808292389 5.870914101600647 13.074591755867004 4.850097969174385 11.946418762207031 4.633240699768066 C 11.68772941827774 4.585637886077166 11.2907133102417 4.5856378227472305 11.035616874694824 4.635885238647461 Z M 20.30531883239746 9.673850059509277 C 19.892134428024292 10.17103499174118 19.385535418987274 10.565080389380455 18.88252830505371 10.779293060302734 C 18.083106338977814 11.11780196428299 17.26212567090988 11.096645832061768 16.475278854370117 10.71053409576416 C 16.243536293506622 10.599460862576962 15.850113227963448 10.332355961203575 15.641724586486816 10.14987850189209 L 15.550106048583984 10.067895889282227 L 15.537530899047852 10.245083808898926 C 15.490823097527027 10.953836858272552 15.341717809438705 11.694325506687164 15.11895751953125 12.342252731323242 C 14.946497946977615 12.836793094873428 14.76685181260109 13.217615604400635 14.463251113891602 13.72537899017334 C 14.393189407885075 13.841741435229778 14.389595782384276 13.854964589700103 14.420135498046875 13.881410598754883 C 14.646488696336746 14.066532656550407 15.230336904525757 14.867846757173538 15.458486557006836 15.304205894470215 C 15.902210682630539 16.153122782707214 16.239944726228714 17.173937559127808 16.496837615966797 18.44334602355957 L 16.55791664123535 18.742185592651367 L 23 18.742185592651367 L 23 18.543842315673828 C 23 18.21855640411377 22.965867809951305 17.467488765716553 22.926345825195312 16.957080841064453 C 22.76825788617134 14.865201473236084 22.351480841636658 12.818280458450317 21.830509185791016 11.588541030883789 C 21.489182949066162 10.781937777996063 21.027493864297867 10.062606811523438 20.53885841369629 9.573355674743652 L 20.458017349243164 9.491373062133789 L 20.30531883239746 9.673850059509277 Z M 2.4377877712249756 9.62624740600586 C 2.116222530603409 9.95153334736824 1.694056048989296 10.54127898812294 1.467702865600586 10.9802827835083 C 1.298836201429367 11.308213323354721 1.097633309662342 11.768374174833298 0.9772709012031555 12.104238510131836 C 0.43833476305007935 13.616950392723083 0.07185814995318651 15.9521324634552 0.012575176544487476 18.24764633178711 L 0 18.742185592651367 L 6.443880081176758 18.72896385192871 L 6.517534255981445 18.3719425201416 C 6.927125722169876 16.40171468257904 7.523548901081085 15.055612921714783 8.421775817871094 14.06917667388916 C 8.520580776035786 13.96074803173542 8.60321766207926 13.862898121587932 8.606810569763184 13.852319717407227 C 8.608607023605146 13.841741313226521 8.561898902058601 13.754469312727451 8.502615928649902 13.65661907196045 C 7.943918764591217 12.760099291801453 7.573849208652973 11.562095046043396 7.475044250488281 10.335000038146973 L 7.453486442565918 10.067895889282227 L 7.363664150238037 10.149877548217773 C 7.155275508761406 10.33235502243042 6.761851951479912 10.599459894001484 6.530109405517578 10.710533142089844 C 5.3534321784973145 11.28705620765686 4.0815430879592896 11.043752253055573 3.0503785610198975 10.046737670898438 C 2.9318126142024994 9.933019824326038 2.773724749684334 9.766410082578659 2.7000701427459717 9.676493644714355 C 2.6264155358076096 9.586577206850052 2.5617431686259806 9.512528357328847 2.556353807449341 9.515172958374023 C 2.550964446272701 9.515172958374023 2.498867202550173 9.56542157754302 2.4377877712249756 9.62624740600586 Z M 13.89197826385498 14.460583686828613 C 13.611731439828873 14.775291204452515 13.164414942264557 15.119089618325233 12.797938346862793 15.301567077636719 C 12.100914239883423 15.648009777069092 11.303288161754608 15.706190168857574 10.586503028869629 15.46024227142334 C 10.087088853120804 15.290987819433212 9.571506887674332 14.947190135717392 9.152933120727539 14.505541801452637 C 9.061313971877098 14.407691568136215 8.993048159405589 14.352154484018683 8.976880073547363 14.362732887268066 C 8.910411283373833 14.39975729957223 8.613996356725693 14.719754621386528 8.434350967407227 14.947190284729004 C 8.261891394853592 15.166692167520523 8.06607848405838 15.473466008901596 7.870265007019043 15.835776329040527 C 7.74630968272686 16.063211992383003 7.512770429253578 16.613289713859558 7.399593830108643 16.943864822387695 C 7.022338509559631 18.038729548454285 6.722330421209335 19.625490069389343 6.571428298950195 21.339191436767578 C 6.537295673042536 21.738526165485382 6.490588188171387 22.5795079767704 6.490588188171387 22.817522048950195 L 6.490588188171387 23 L 16.52018928527832 23 L 16.509410858154297 22.608598709106445 C 16.45551723986864 20.794402480125427 16.15371337532997 18.77128303050995 15.720767974853516 17.31410789489746 C 15.357884258031845 16.092302322387695 14.844099402427673 15.137600898742676 14.195579528808594 14.484384536743164 C 14.109349742531776 14.397112712264061 14.032101242803037 14.325708327349275 14.02311897277832 14.328352928161621 C 14.015933156944811 14.328352928161621 13.956650607287884 14.389179468154907 13.89197826385498 14.460583686828613 Z"
        val path = PathParser.createPathFromPathData(pathData)
        val rectF = RectF()
        path.computeBounds(rectF, true)
        val matrix = Matrix()
        val scale = minOf(size.width / rectF.width(), size.height / rectF.height())
        matrix.setScale(scale, scale)
        path.transform(matrix)
        drawPath(path = path.asComposePath(), color = FigmaTextWhite, style = Fill)
    }
}

@Composable
private fun PlusIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val pathData = "M 10.5 0 C 4.706417145936386 0 0 4.706417145936386 0 10.5 C 0 16.293582854063615 4.706417145936386 21 10.5 21 C 16.293582854063615 21 21 16.293582854063615 21 10.5 C 21 4.706417145936386 16.293582854063615 0 10.5 0 Z M 10.5 0.9130434782608695 C 15.800137125927469 0.9130434782608695 20.08695652173913 5.199862874072531 20.08695652173913 10.5 C 20.08695652173913 15.800137125927469 15.800137125927469 20.08695652173913 10.5 20.08695652173913 C 5.199862874072531 20.08695652173913 0.9130434782608695 15.800137125927469 0.9130434782608695 10.5 C 0.9130434782608695 5.199862874072531 5.199862874072531 0.9130434782608695 10.5 0.9130434782608695 Z M 10.043478260869565 5.021739130434782 L 10.043478260869565 10.043478260869565 L 5.021739130434782 10.043478260869565 L 5.021739130434782 10.956521739130434 L 10.043478260869565 10.956521739130434 L 10.043478260869565 15.978260869565217 L 10.956521739130434 15.978260869565217 L 10.956521739130434 10.956521739130434 L 15.978260869565217 10.956521739130434 L 15.978260869565217 10.043478260869565 L 10.956521739130434 10.043478260869565 L 10.956521739130434 5.021739130434782 L 10.043478260869565 5.021739130434782 Z"
        val path = PathParser.createPathFromPathData(pathData)
        val rectF = RectF()
        path.computeBounds(rectF, true)
        val matrix = Matrix()
        val scale = minOf(size.width / rectF.width(), size.height / rectF.height())
        matrix.setScale(scale, scale)
        path.transform(matrix)
        drawPath(path = path.asComposePath(), color = FigmaGold, style = Fill)
    }
}

@Composable
private fun SaveIcon(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Placeholder for save icon
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true, widthDp = 389, heightDp = 879)
@Composable
fun NewCaseScreenPreview() {
    LegallyTheme {
        NewCaseScreen()
    }
}
